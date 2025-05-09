package org.example.service;

import org.example.util.ItemCF;
import org.example.util.UserCF;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RecommendationService {

    /**
     * 根据用户评分数据进行推荐，融合基于用户和基于物品的推荐。
     *
     * @param ratings 用户评分数据
     * @param userId  用户ID
     * @param numRecommendations 推荐数量
     * @return 推荐物品的ID列表
     */
    public List<Integer> getRecommendations(Map<Integer, Map<Integer, Integer>> ratings, int userId, int numRecommendations) {
        // 获取基于用户的推荐
        List<Integer> userCFRecommendations = UserCF.recommend(ratings, userId, numRecommendations);

        // 获取基于物品的推荐
        List<Integer> itemCFRecommendations = ItemCF.recommend(ratings, userId, numRecommendations);

        // 你可以在此处对两个推荐结果进行融合，例如取并集、交集、加权平均等
        // 这里仅示范返回基于用户和物品的并集推荐结果
        userCFRecommendations.addAll(itemCFRecommendations);

        return userCFRecommendations;  // 返回融合后的推荐结果（需要去重）
    }
}
