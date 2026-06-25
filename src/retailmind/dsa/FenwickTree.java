package retailmind.dsa;

/**
 * Fenwick Tree for cumulative revenue sums.
 */
public class FenwickTree {
    private final double[] tree;

    /**
     * Creates a Fenwick Tree with a fixed size.
     *
     * @param size number of values
     */
    public FenwickTree(int size) {
        tree = new double[size + 1];
    }

    /**
     * Builds a Fenwick Tree from revenue values.
     *
     * @param values revenue values
     * @return built Fenwick Tree
     */
    public static FenwickTree fromArray(double[] values) {
        FenwickTree fenwickTree = new FenwickTree(values == null ? 0 : values.length);
        if (values != null) {
            for (int index = 0; index < values.length; index++) {
                fenwickTree.update(index + 1, values[index]);
            }
        }
        return fenwickTree;
    }

    /**
     * Adds a value at a one-based day index.
     *
     * @param day one-based day
     * @param value value to add
     */
    public void update(int day, double value) {
        while (day < tree.length) {
            tree[day] += value;
            day += day & -day;
        }
    }

    /**
     * Gets cumulative revenue from day 1 through the requested day.
     *
     * @param day one-based day
     * @return cumulative revenue
     */
    public double getPrefixSum(int day) {
        double sum = 0;
        while (day > 0) {
            sum += tree[day];
            day -= day & -day;
        }
        return sum;
    }
}
