package org.example.util;

import java.util.*;

public class UserCF {

    /**
     * 推荐方法，根据用户的评分数据和用户ID生成推荐物品。
     * @param ratings 包含所有用户及其对应评分数据的映射。外层键为用户ID，内层键为物品ID，值为对应评分。
     * @param userId  目标用户的ID，用于生成针对该用户的推荐物品。
     * @param numRecommendations 推荐数量，决定了最终返回推荐列表的长度。
     * @return 返回一个包含推荐物品ID的列表，列表长度由numRecommendations决定或者受限于可推荐物品数量（取较小值）。
     */
    public static List<Integer> recommend(Map<Integer, Map<Integer, Integer>> ratings, int userId, int numRecommendations) {
        // 创建一个Map，用于存储每个物品的加权评分。键为物品ID，值为加权评分（初始化为0.0）。
        Map<Integer, Double> weightedRatings = new HashMap<>();
        // 创建一个Map，用于存储每个其他用户与目标用户的相似度得分。键为其他用户的ID，值为相似度得分（初始化为0.0）。
        Map<Integer, Double> similarityScores = new HashMap<>();

        // 计算与其他用户的相似度，并基于相似度和其他用户的评分计算物品的加权评分
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : ratings.entrySet()) {
            int otherUserId = entry.getKey();
            if (otherUserId == userId) continue;

            // 计算相似度
            double similarity = calculateSimilarity(ratings.get(userId), entry.getValue());
            similarityScores.put(otherUserId, similarity);

            // 遍历其他用户评分过的物品，计算这些物品的加权评分
            for (Map.Entry<Integer, Integer> item : entry.getValue().entrySet()) {
                int itemId = item.getKey();
                double rating = item.getValue();

                // 如果目标用户没有对该物品进行过评分，则将该物品的加权评分加上当前其他用户的评分乘以相似度
                if (ratings.get(userId).get(itemId) == null) {
                    weightedRatings.put(itemId, weightedRatings.getOrDefault(itemId, 0.0) + rating * similarity);
                }
            }
        }

        // 将加权评分的Map转换为List<Map.Entry<Integer, Double>>，方便后续进行排序操作
        List<Map.Entry<Integer, Double>> sortedRecommendations = new ArrayList<>(weightedRatings.entrySet());
        sortedRecommendations.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        List<Integer> recommendedItems = new ArrayList<>();
        for (int i = 0; i < Math.min(numRecommendations, sortedRecommendations.size()); i++) {
            recommendedItems.add(sortedRecommendations.get(i).getKey());
        }

        return recommendedItems;
    }

    /**
     * 计算用户和其他用户之间相似度的方法。
     * @param userRatings 当前用户的评分数据
     * @param otherUserRatings 另一个用户的评分数据
     * @return 返回两个用户之间的相似度得分（0-1之间）。
     */
    private static double calculateSimilarity(Map<Integer, Integer> userRatings, Map<Integer, Integer> otherUserRatings) {
        double dotProduct = 0.0;
        double userMagnitude = 0.0;
        double otherUserMagnitude = 0.0;

        for (Map.Entry<Integer, Integer> entry : userRatings.entrySet()) {
            int itemId = entry.getKey();
            if (otherUserRatings.containsKey(itemId)) {
                dotProduct += entry.getValue() * otherUserRatings.get(itemId);
                userMagnitude += Math.pow(entry.getValue(), 2);
                otherUserMagnitude += Math.pow(otherUserRatings.get(itemId), 2);
            }
        }

        if (userMagnitude == 0 || otherUserMagnitude == 0) return 0;

        return dotProduct / (Math.sqrt(userMagnitude) * Math.sqrt(otherUserMagnitude));
    }
}
