package com.sample.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.sample.dto.response.LoadingPageResponse;
import com.sample.dto.response.UserResponse;
import com.sample.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.*;

@Repository
@Slf4j
@RequiredArgsConstructor
public class UserRepository {

    private final DynamoDBMapper dynamoDBMapper;

    /**
     * Save user to dynamodb
     *
     * @param user
     * @return
     */
    public User save(User user) {
        log.info("Saving user, object={}", user);

        dynamoDBMapper.save(user);
        log.info("Sample user has saved");
        return user;
    }

    /**
     * Update user to dynamodb
     *
     * @param user
     * @param hashKey
     * @return
     */
    public String update(User user, String hashKey) {
        log.info("Updating user by composite key hashKey={}, rangeKey={}", hashKey, user.getRangeKey());

        dynamoDBMapper.save(user,
                new DynamoDBSaveExpression()
                        .withExpectedEntry("hashKey",
                                new ExpectedAttributeValue(
                                        new AttributeValue().withS(hashKey)
                                )));
        log.info("Update user by composite key hashKey={}, rangeKey={}", hashKey, user.getRangeKey());
        return hashKey;
    }

    /**
     * Delete user from dynamodb
     *
     * @param hashKey
     * @param rangeKey
     * @return
     */
    public String delete(String hashKey, String rangeKey) {
        log.info("Deleting user by composite key hashKey={}, rangeKey={}", hashKey, rangeKey);

        dynamoDBMapper.delete(User.builder()
                .hashKey(hashKey)
                .rangeKey(rangeKey).build());
        return rangeKey;
    }

    /**
     * Find user from to dynamodb
     *
     * @param hashKey
     * @param rangeKey
     * @return User
     */
    public User findByCompositeKey(String hashKey, String rangeKey) {
        log.info("Finding user by composite key hashKey={}, rangeKey={}", hashKey, rangeKey);

        return dynamoDBMapper.load(User.class, hashKey, rangeKey);
    }

    /**
     * Find user by phone
     *
     * @param phone
     * @return User
     */
    public User findByPhone(String phone) {
        log.info("Finding user by phone ...");

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":phone", new AttributeValue().withS(phone));

        DynamoDBQueryExpression<User> queryExpression = new DynamoDBQueryExpression<User>()
                .withIndexName("phone-index")
                .withKeyConditionExpression("phone = :phone")
                .withExpressionAttributeValues(expressionAttributeValues)
                .withConsistentRead(false);
        PaginatedQueryList<User> result = dynamoDBMapper.query(User.class, queryExpression);

        if (!result.isEmpty()) {
            log.info("Found out user, phone={} in DynamoDB", phone);
            return result.get(0);
        }
        return null;
    }

    /**
     * Find user by email
     *
     * @param email
     * @return User
     */
    public User findByEmail(String email) {
        log.info("Finding user by email={}", email);

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":email", new AttributeValue().withS(email));

        DynamoDBQueryExpression<User> queryExpression = new DynamoDBQueryExpression<User>()
                .withIndexName("email-index")
                .withKeyConditionExpression("email = :email")
                .withExpressionAttributeValues(expressionAttributeValues)
                .withConsistentRead(false);
        PaginatedQueryList<User> result = dynamoDBMapper.query(User.class, queryExpression);

        if (!result.isEmpty()) {
            log.info("Found out user, email={} in DynamoDB", email);
            return result.get(0);
        }
        return null;
    }

    /**
     * Find all user by multiple conditions
     *
     * @param hashKey
     * @param search
     * @param firstName
     * @param lastName
     * @param phone
     * @param email
     * @param address
     * @param status
     * @param nextKey
     * @param pageSize
     * @return
     */
    public LoadingPageResponse findAll(String hashKey, String search, String firstName, String lastName, String phone, String email, String address, String status, String orderBy, String nextKey, int pageSize) {
        log.info("Finding user from DynamoBD ...");

        DynamoDBQueryExpression<User> queryExpression = new DynamoDBQueryExpression<User>()
                .withHashKeyValues(User.builder().hashKey(hashKey).build())
                .withConsistentRead(false);

        Map<String, String> ean = new HashMap<>();
        Map<String, AttributeValue> eav = new HashMap<>();
        StringBuilder filterExpression = new StringBuilder();

        if (StringUtils.hasLength(search)) { // search free text
            eav.put(":keyword", new AttributeValue().withS(search));
            filterExpression.append("contains(searchKeys, :keyword)");
            log.info("search free text into field searchKey");
            log.info("Finding user with keyword={}", search);
        } else { // search advance
            if (StringUtils.hasLength(firstName)) {
                eav.put(":firstName", new AttributeValue().withS(firstName));
                filterExpression.append("firstName = :firstName ");
            }

            if (StringUtils.hasLength(lastName)) {
                if (StringUtils.hasLength(filterExpression)) {
                    filterExpression.append("AND ");
                }
                eav.put(":lastName", new AttributeValue().withS(lastName));
                filterExpression.append("lastName = :lastName ");
            }

            if (StringUtils.hasLength(phone)) {
                if (StringUtils.hasLength(filterExpression)) {
                    filterExpression.append("AND ");
                }
                eav.put(":phone", new AttributeValue().withS(phone));
                filterExpression.append("phone = :phone ");
            }

            if (StringUtils.hasLength(email)) {
                if (StringUtils.hasLength(filterExpression)) {
                    filterExpression.append("AND ");
                }
                eav.put(":email", new AttributeValue().withS(email));
                filterExpression.append("email = :email ");
            }

            if (StringUtils.hasLength(address)) {
                eav.put(":address", new AttributeValue().withS(address));
                filterExpression.append("(address.text = :address) ");
            }

            if (StringUtils.hasLength(status)) {
                if (StringUtils.hasLength(status)) {
                    eav.put(":status", new AttributeValue().withS(status.toUpperCase()));
                    filterExpression.append("(status = :status) ");
                }
            }
        }

        if (!filterExpression.isEmpty()) {
            queryExpression.withFilterExpression(filterExpression.toString());
            queryExpression.withExpressionAttributeValues(eav);
        }

        if (StringUtils.hasLength(nextKey)) {
            Map<String, AttributeValue> keys = new LinkedHashMap<>();
            AttributeValue hashValue = new AttributeValue(hashKey);
            AttributeValue rangeValue = new AttributeValue(nextKey);
            keys.put("hashKey", hashValue);
            keys.put("rangeKey", rangeValue);
            queryExpression.setExclusiveStartKey(keys);
        }

        // sort by firstName-index desc
        if (StringUtils.hasLength(orderBy)) {
            queryExpression.withIndexName("firstName-index").withScanIndexForward(false);
        }

        List<User> result = new ArrayList<>();
        QueryResultPage<User> resultPage;

        // process case: result < expected result
        int limit = 100;
        do {
            queryExpression.withLimit(limit);
            resultPage = dynamoDBMapper.queryPage(User.class, queryExpression);
            queryExpression.setExclusiveStartKey(resultPage.getLastEvaluatedKey());
            result.addAll(resultPage.getResults());
            limit = 300000;

            log.info("queryExpression: {}", queryExpression);
        } while (result.size() < pageSize && resultPage.getLastEvaluatedKey() != null);

        if (pageSize > result.size()) pageSize = result.size() - 1;

        result = result.subList(0, pageSize);

        String rangeKey = "";
        if (!result.isEmpty()) {
            rangeKey = result.get(pageSize - 1).getRangeKey();
        }

        List<UserResponse> items = result.stream().map(
                x -> UserResponse.builder()
                        .id(x.getRangeKey())
                        .firstName(x.getFirstName())
                        .lastName(x.getLastName())
                        .phone(x.getPhone())
                        .email(x.getEmail())
                        .address(x.getAddress())
                        .build()
        ).toList();

        return LoadingPageResponse.builder()
                .nextKey(rangeKey)
                .items(items)
                .build();
    }
}
