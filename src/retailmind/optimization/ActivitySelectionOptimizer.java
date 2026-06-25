package retailmind.optimization;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the Activity Selection Algorithm (Greedy) for Promotion Scheduling.
 */
public class ActivitySelectionOptimizer {

    /**
     * Chooses the maximum number of non-overlapping promotions.
     * Assumes the input list is unsorted.
     *
     * @param promotions list of available promotions
     * @return list of scheduled non-overlapping promotions
     */
    public static List<Promotion> schedulePromotions(List<Promotion> promotions) {
        if (promotions == null || promotions.isEmpty()) {
            return new ArrayList<>();
        }

        // Sort promotions by finish time (Greedy choice)
        List<Promotion> sortedPromotions = new ArrayList<>(promotions);
        sortedPromotions.sort((p1, p2) -> Integer.compare(p1.getEndTime(), p2.getEndTime()));

        List<Promotion> selectedPromotions = new ArrayList<>();
        
        // The first activity always gets selected
        Promotion currentPromo = sortedPromotions.get(0);
        selectedPromotions.add(currentPromo);

        // Consider rest of the activities
        for (int i = 1; i < sortedPromotions.size(); i++) {
            Promotion nextPromo = sortedPromotions.get(i);
            // If this activity has start time greater than or equal to
            // the finish time of previously selected activity, then select it
            if (nextPromo.getStartTime() >= currentPromo.getEndTime()) {
                selectedPromotions.add(nextPromo);
                currentPromo = nextPromo;
            }
        }

        return selectedPromotions;
    }
}
