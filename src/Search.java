import java.time.Duration;
import java.time.Instant;

public class Search {

    private Search() { }

    public static Action miniMax(Board state, int depth) {
        return maxValueState(state, depth).getLastAction();
    }

    public static Action alphaBeta(Board state, int depth) {
        return maxValueStateAlphaBeta(state, depth, Integer.MIN_VALUE, Integer.MAX_VALUE).getLastAction();
    }

    public static Action alphaBetaTimer(Board state, int depth) {
        return maxValueStateAlphaBetaTimer(state, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, Instant.now()).getLastAction();
    }

    private static Board maxValueState(Board state, int depth) {

        if (state.isTerminal() || depth == 0) { return state; }

        int maxValue = Integer.MIN_VALUE;
        Board maxState = null;

        for(Board resultBoard : state.getActions(Player.BLACK)) {

            int minValue = minValueState(resultBoard, depth - 1).getUtilityValue();

            if (maxValue < minValue) {
                maxValue = minValue;
                maxState = resultBoard;
                maxState.setUtilityValue(maxValue);
            }
        }

        return maxState;
    }

    private static Board minValueState(Board state, int depth) {
        if (state.isTerminal() || depth == 0) { return state; }

        int minValue = Integer.MAX_VALUE;
        Board minState = null;

        for(Board resultBoard : state.getActions(Player.WHITE)) {
            int maxValue = maxValueState(resultBoard, depth - 1).getUtilityValue();

            if (maxValue < minValue) {
                minValue = maxValue;
                minState = resultBoard;
                minState.setUtilityValue(minValue);
            }
        }

        return minState;
    }

    private static Board maxValueStateAlphaBeta(Board state, int depth, int alpha, int beta) {

        if (state.isTerminal() || depth == 0) { return state; }

        int maxValue = Integer.MIN_VALUE;
        Board maxState = null;

        for(Board resultBoard : state.getActions(Player.BLACK)) {

            int minValue = minValueStateAlphaBeta(resultBoard, depth - 1, alpha, beta).getUtilityValue();

            if (maxValue < minValue) {
                maxValue = minValue;
                maxState = resultBoard;
                maxState.setUtilityValue(maxValue);
            }

            if (maxValue >= beta) { return maxState; }

            alpha = Math.max(alpha, maxValue);

        }

        return maxState;
    }

    private static Board minValueStateAlphaBeta(Board state, int depth, int alpha, int beta) {
        if (state.isTerminal() || depth == 0) { return state; }

        int minValue = Integer.MAX_VALUE;
        Board minState = null;

        for(Board resultBoard : state.getActions(Player.WHITE)) {
            int maxValue = maxValueStateAlphaBeta(resultBoard, depth - 1, alpha, beta).getUtilityValue();

            if (maxValue < minValue) {
                minValue = maxValue;
                minState = resultBoard;
                minState.setUtilityValue(minValue);
            }

            if (minValue <= alpha) { return minState; }

            beta = Math.min(beta, minValue);
        }

        return minState;
    }

    private static Board maxValueStateAlphaBetaTimer(Board state, int depth, int alpha, int beta, Instant startTime) {
        Instant currentTime = Instant.now();
        Long duration = Duration.between(startTime, currentTime).toMillis();

        if (state.isTerminal() || depth == 0 || duration > 10000) {
            return state;
        }

        int maxValue = Integer.MIN_VALUE;
        Board maxState = null;

        for(Board resultBoard : state.getActions(Player.BLACK)) {

            int minValue = minValueStateAlphaBetaTimer(resultBoard, depth - 1, alpha, beta, startTime).getUtilityValue();

            if (maxValue < minValue) {
                maxValue = minValue;
                maxState = resultBoard;
                maxState.setUtilityValue(maxValue);
            }

            if (maxValue >= beta) { return maxState; }

            alpha = Math.max(alpha, maxValue);

        }

        return maxState;
    }

    private static Board minValueStateAlphaBetaTimer(Board state, int depth, int alpha, int beta, Instant startTime) {
        Instant currentTime = Instant.now();
        Long duration = Duration.between(startTime, currentTime).toMillis();

        if (state.isTerminal() || depth == 0 || duration > 10000) {
            return state;
        }

        int minValue = Integer.MAX_VALUE;
        Board minState = null;

        for(Board resultBoard : state.getActions(Player.WHITE)) {
            int maxValue = maxValueStateAlphaBetaTimer(resultBoard, depth - 1, alpha, beta, startTime).getUtilityValue();

            if (maxValue < minValue) {
                minValue = maxValue;
                minState = resultBoard;
                minState.setUtilityValue(minValue);
            }

            if (minValue <= alpha) { return minState; }

            beta = Math.min(beta, minValue);
        }

        return minState;
    }

}
