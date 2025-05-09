package org.example.util;

import java.util.*;

public class ItemCF {

    /**
     * 推荐方法，根据用户的评分数据和用户ID生成推荐物品。
     * @param ratings 包含所有用户及其对应评分数据的映射。外层键为用户ID，内层键为物品ID，值为对应评分。
     * @param userId  目标用户的ID，用于生成针对该用户的推荐物品。
     * @param numRecommendations 推荐数量，决定了最终返回推荐列表的长度。
     * @return 返回一个包含推荐物品ID的列表，列表长度由numRecommendations决定或者受限于可推荐物品数量（取较小值）。
     */
    public static List<Integer> recommend(Map<Integer, Map<Integer, Integer>> ratings, int userId, int numRecommendations) {
        // 1. 构建物品-物品相似度矩阵
        Map<Integer, Map<Integer, Double>> itemSimilarity = calculateItemSimilarity(ratings);

        // 2. 获取目标用户已评分物品
        Map<Integer, Integer> userRatings = ratings.get(userId);
        if (userRatings == null) return new ArrayList<>();

        // 3. 对每个未评分物品进行加权得分计算
        Map<Integer, Double> scores = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : userRatings.entrySet()) {
            int ratedItem = entry.getKey();
            double rating = entry.getValue();

            Map<Integer, Double> similarItems = itemSimilarity.get(ratedItem);
            if (similarItems == null) continue;

            for (Map.Entry<Integer, Double> simEntry : similarItems.entrySet()) {
                int itemId = simEntry.getKey();
                if (userRatings.containsKey(itemId)) continue; // 已评分不推荐

                double sim = simEntry.getValue();
                scores.put(itemId, scores.getOrDefault(itemId, 0.0) + sim * rating);
            }
        }

        // 4. 按分数排序，返回前 N 个推荐
        List<Map.Entry<Integer, Double>> sorted = new ArrayList<>(scores.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        List<Integer> recommended = new ArrayList<>();
        for (int i = 0; i < Math.min(numRecommendations, sorted.size()); i++) {
            recommended.add(sorted.get(i).getKey());
        }

        return recommended;
    }

    /**
     * 计算物品之间的相似度（余弦相似度）
     * @param ratings 用户-物品评分数据，格式：userId -> (itemId -> rating)
     * @return 物品相似度矩阵
     */
    private static Map<Integer, Map<Integer, Double>> calculateItemSimilarity(Map<Integer, Map<Integer, Integer>> ratings) {
        Map<Integer, Map<Integer, Double>> similarity = new HashMap<>();

        // 统计每个物品的用户评分向量
        Map<Integer, Map<Integer, Integer>> itemUserRatings = new HashMap<>();
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : ratings.entrySet()) {
            int userId = entry.getKey();
            for (Map.Entry<Integer, Integer> itemRating : entry.getValue().entrySet()) {
                int itemId = itemRating.getKey();
                itemUserRatings.computeIfAbsent(itemId, k -> new HashMap<>()).put(userId, itemRating.getValue());
            }
        }

        // 两两计算相似度
        List<Integer> items = new ArrayList<>(itemUserRatings.keySet());
        for (int i = 0; i < items.size(); i++) {
            int itemA = items.get(i);
            for (int j = i + 1; j < items.size(); j++) {
                int itemB = items.get(j);
                double sim = cosineSimilarity(itemUserRatings.get(itemA), itemUserRatings.get(itemB));
                if (sim == 0) continue;

                similarity.computeIfAbsent(itemA, k -> new HashMap<>()).put(itemB, sim);
                similarity.computeIfAbsent(itemB, k -> new HashMap<>()).put(itemA, sim);
            }
        }

        return similarity;
    }

    /**
     * 计算两个物品向量的余弦相似度
     * @param ratingsA 物品A的评分数据
     * @param ratingsB 物品B的评分数据
     * @return 返回两个物品的余弦相似度
     */
    private static double cosineSimilarity(Map<Integer, Integer> ratingsA, Map<Integer, Integer> ratingsB) {
        double dot = 0.0, normA = 0.0, normB = 0.0;
        for (Integer user : ratingsA.keySet()) {
            if (ratingsB.containsKey(user)) {
                dot += ratingsA.get(user) * ratingsB.get(user);
            }
        }
        for (int r : ratingsA.values()) normA += r * r;
        for (int r : ratingsB.values()) normB += r * r;

        if (normA == 0 || normB == 0) return 0;

        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}

