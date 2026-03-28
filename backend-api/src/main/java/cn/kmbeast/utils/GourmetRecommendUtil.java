package cn.kmbeast.utils;

import cn.kmbeast.pojo.entity.Gourmet;
import cn.kmbeast.pojo.entity.Interaction;
import cn.kmbeast.pojo.vo.GourmetVO;
import cn.kmbeast.pojo.vo.InteractionVO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Method to find out recommended gourmet articals
 * score = weight_a x category-similarity + weight_b x hotness + weight_c x rating
 * hotness = weight_1 x likes + weight_1 x saves + weight_3 x views
 */
public class GourmetRecommendUtil {
    // weight for score
    private static final double W_CATEGORY = 0.4;
    private static final double W_HOTNESS  = 0.4;
    private static final double W_RATING   = 0.2;

    // weight for hotness
    private static final double W_LIKE = 0.5;
    private static final double W_VIEW = 0.2;
    private static final double W_SAVE = 0.3;

    @Data
    @AllArgsConstructor
    public static class GourmetScore{
        private Gourmet gourmet;
        private double score;
    }

    /**
     * find top N Gourmet with hightest scores
     * @param target
     * @param allGourmets
     * @param topN
     * @return
     */
    public static List<GourmetScore> recommend(
            GourmetVO target,
            List<GourmetVO> allGourmets,
            int topN){

        // find the highest score for hotness among all gourmets, used later for score normalization
        double maxHotness = allGourmets.stream() //List<Gourmet>
                .mapToDouble(GourmetRecommendUtil::calcHotness)
                .max().orElse(1.0); //orElse!=0 incase of Arithmetic Error //Double

        return allGourmets.stream()
                .filter(g -> !g.getId().equals(target.getId())) //rule out the target Gourmet itself
                .map(g->{
                    double catScore = target.getCategoryId().equals(g.getId()) ? 1.0 : 0.0; // 三元表达式：条件 ? 真值 : 假值
                    double hotScore = calcHotness(g)/maxHotness; // normalization of hotness
                    double rateScore = calcRating(g);
                    double finalScore = W_CATEGORY*catScore + W_HOTNESS * hotScore + W_RATING * rateScore;
                    return new GourmetScore(g, finalScore);
                })
                .sorted(Comparator.comparingDouble(GourmetScore::getScore).reversed()) //DESC order
                .limit(topN) // get top N results
                .collect(Collectors.toList()); // Stream<GourmetScore>
    }

    /**
     * calculating hotness
     * @param gourmetVO
     * @return score for hotness
     */
    public static double calcHotness(GourmetVO gourmetVO){
       int likes = gourmetVO.getUpvoteCount() != null? gourmetVO.getUpvoteCount() : 0;
       int views = gourmetVO.getViewCount() != null? gourmetVO.getViewCount() : 0;
       int saves = gourmetVO.getSaveCount() != null? gourmetVO.getSaveCount() : 0;
        return W_LIKE * likes + W_VIEW * views + W_SAVE * saves;
        }

    /**
     * calculating normalized rating ( Min-Max Scaling)
     * @param gourmetVO
     * @return score for rating(normalized)
     */
    public static double calcRating(GourmetVO gourmetVO){
        if (gourmetVO.getRating() == null || gourmetVO.getRating() == 0) return 0.0;
        return (gourmetVO.getRating() - 1) / 4.0;
    }
}
