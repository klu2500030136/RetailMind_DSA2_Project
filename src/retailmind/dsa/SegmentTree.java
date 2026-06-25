package retailmind.dsa;

/**
 * Segment Tree for range sum queries on daily revenue.
 */
public class SegmentTree {
    private double[] tree;
    private double[] values;

    /**
     * Creates a Segment Tree with initial revenue values.
     *
     * @param values revenue values
     */
    public SegmentTree(double[] values) {
        build(values);
    }

    /**
     * Builds or rebuilds the tree.
     *
     * @param inputValues revenue values
     */
    public void build(double[] inputValues) {
        values = inputValues == null ? new double[0] : inputValues.clone();
        tree = new double[Math.max(1, values.length * 4)];
        if (values.length > 0) {
            build(1, 0, values.length - 1);
        }
    }

    /**
     * Queries the revenue sum between two zero-based indexes.
     *
     * @param left left index
     * @param right right index
     * @return revenue sum
     */
    public double query(int left, int right) {
        if (values.length == 0 || left < 0 || right >= values.length || left > right) {
            return 0;
        }
        return query(1, 0, values.length - 1, left, right);
    }

    /**
     * Updates one revenue value.
     *
     * @param index zero-based index
     * @param newValue updated revenue
     */
    public void update(int index, double newValue) {
        if (index < 0 || index >= values.length) {
            return;
        }
        update(1, 0, values.length - 1, index, newValue);
    }

    /**
     * Recursively builds tree values.
     *
     * @param node tree node index
     * @param start segment start
     * @param end segment end
     */
    private void build(int node, int start, int end) {
        if (start == end) {
            tree[node] = values[start];
            return;
        }

        int middle = (start + end) / 2;
        build(node * 2, start, middle);
        build((node * 2) + 1, middle + 1, end);
        tree[node] = tree[node * 2] + tree[(node * 2) + 1];
    }

    /**
     * Recursively answers a range sum query.
     *
     * @param node tree node index
     * @param start segment start
     * @param end segment end
     * @param left query start
     * @param right query end
     * @return range sum
     */
    private double query(int node, int start, int end, int left, int right) {
        if (right < start || end < left) {
            return 0;
        }
        if (left <= start && end <= right) {
            return tree[node];
        }

        int middle = (start + end) / 2;
        return query(node * 2, start, middle, left, right)
                + query((node * 2) + 1, middle + 1, end, left, right);
    }

    /**
     * Recursively updates one value.
     *
     * @param node tree node index
     * @param start segment start
     * @param end segment end
     * @param index target index
     * @param newValue updated value
     */
    private void update(int node, int start, int end, int index, double newValue) {
        if (start == end) {
            values[index] = newValue;
            tree[node] = newValue;
            return;
        }

        int middle = (start + end) / 2;
        if (index <= middle) {
            update(node * 2, start, middle, index, newValue);
        } else {
            update((node * 2) + 1, middle + 1, end, index, newValue);
        }

        tree[node] = tree[node * 2] + tree[(node * 2) + 1];
    }
}
