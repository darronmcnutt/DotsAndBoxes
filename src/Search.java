public class Search {

    private Search() {
    }

    /**
     * Returns an action object representing the move that leads to the outcome with the best utility
     * @param state Board object to search
     * @param depth maximum search depth
     * @return an Action object representing the move that leads to the outcome with the best utility
     */
    public static Action miniMax(Board state, int depth) {
        Board resultBoard = maxValueState(state, depth);
        System.out.println("PROJECTED UTILITY: " + resultBoard.getUtilityValue());
        return resultBoard.getLastAction();
    }

    /**
     * Returns an action object representing the move that leads to the outcome with the best utility
     * Uses alpha-beta pruning
     * @param state Board object to search
     * @param depth maximum search depth
     * @return an Action object representing the move that leads to the outcome with the best utility
     */
    public static Action alphaBeta(Board state, int depth) {
        Board resultBoard = maxValueStateAlphaBeta(state, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
        System.out.println("PROJECTED UTILITY: " + resultBoard.getUtilityValue());
        return resultBoard.getLastAction();
    }

    /**
     * Recursive helper function for Minimax - represents the Max player
     * @param state Board object to search
     * @param depth Maximum search depth
     * @return Board object containing the state of the best move for Max, the Action object representing the move, and
     * the best utility value found
     */
    private static Board maxValueState(Board state, int depth) {

        if (state.isTerminal() || depth == 0) {
            return state;
        }

        int maxValue = Integer.MIN_VALUE;
        Board maxState = null;

        for (Board resultBoard : state.getActions(Player.BLACK)) {

            int minValue = minValueState(resultBoard, depth - 1).getUtilityValue();

            // Max(maxValue, minValue)
            if (maxValue < minValue) {
                maxValue = minValue;
                maxState = resultBoard;
                maxState.setUtilityValue(maxValue);
            }
        }

        return maxState;
    }

    /**
     * Recursive helper function for Minimax - represents the Min player
     * @param state Board object to search
     * @param depth Maximum search depth
     * @return Board object containing the state of the best move for Min, the Action object representing the move, and
     * the best utility value found
     */
    private static Board minValueState(Board state, int depth) {
        if (state.isTerminal() || depth == 0) {
            return state;
        }

        int minValue = Integer.MAX_VALUE;
        Board minState = null;

        for (Board resultBoard : state.getActions(Player.WHITE)) {
            int maxValue = maxValueState(resultBoard, depth - 1).getUtilityValue();

            // Min(minValue, maxValue)
            if (maxValue < minValue) {
                minValue = maxValue;
                minState = resultBoard;
                minState.setUtilityValue(minValue);
            }
        }

        return minState;
    }

    /**
     * Recursive helper function for Alpha-beta pruning - represents the Max player
     * @param state Board object to search
     * @param depth Maximum depth
     * @param alpha Alpha value
     * @param beta Beta value
     * @return Board object containing the state of the best move for Max, the Action object representing the move, and
     * the best utility value found
     */
    private static Board maxValueStateAlphaBeta(Board state, int depth, int alpha, int beta) {

        if (state.isTerminal() || depth == 0) {
            return state;
        }

        int maxValue = Integer.MIN_VALUE;
        Board maxState = null;

        for (Board resultBoard : state.getActions(Player.BLACK)) {

            int minValue = minValueStateAlphaBeta(resultBoard, depth - 1, alpha, beta).getUtilityValue();

            if (maxValue < minValue) {
                maxValue = minValue;
                maxState = resultBoard;
                maxState.setUtilityValue(maxValue);
            }

            if (maxValue >= beta) {
                return maxState;
            }

            alpha = Math.max(alpha, maxValue);

        }

        return maxState;
    }

    /**
     * Recursive helper function for Alpha-beta pruning - represents the Min player
     * @param state Board object to search
     * @param depth Maximum depth
     * @param alpha Alpha value
     * @param beta Beta value
     * @return Board object containing the state of the best move for Min, the Action object representing the move, and
     * the best utility value found
     */
    private static Board minValueStateAlphaBeta(Board state, int depth, int alpha, int beta) {
        if (state.isTerminal() || depth == 0) {
            return state;
        }

        int minValue = Integer.MAX_VALUE;
        Board minState = null;

        for (Board resultBoard : state.getActions(Player.WHITE)) {
            int maxValue = maxValueStateAlphaBeta(resultBoard, depth - 1, alpha, beta).getUtilityValue();

            if (maxValue < minValue) {
                minValue = maxValue;
                minState = resultBoard;
                minState.setUtilityValue(minValue);
            }

            if (minValue <= alpha) {
                return minState;
            }

            beta = Math.min(beta, minValue);
        }

        return minState;
    }

}
