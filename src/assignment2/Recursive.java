package assignment2;

import java.util.ArrayList;
import java.util.*;
import java.util.stream.*;

public class Recursive {

    /**
     * @require The array cost is not null, and all the integers in the array
     *          (the daily costs of the worker) are greater than or equal to
     *          zero. (The number of days that you are running the company is
     *          defined to be n = cost.length).
     * 
     *          The minimum number of days between shifts is greater than or
     *          equal to one (1 <= minShiftBreak). The maximum shift length is
     *          greater than or equal to one (1 <= maxShiftLength).
     * 
     *          The array jobs is not null, and does not contain null values.
     *          The jobs are sorted in ascending order of their end days. The
     *          end day of every job must be strictly less than the length of
     *          the cost array (n = cost.length).
     * 
     * @ensure Returns the maximum profit that can be earned by you for your
     *         company given parameters cost, minShiftBreak, maxShiftLength and
     *         jobs.
     * 
     *         (See handout for details.)
     * 
     *         This method must be implemented using a recursive programming
     *         solution to the problem. It is expected to have a worst-case
     *         running time that is exponential in m = jobs.length. (You must
     *         NOT provide a dynamic programming solution to this question.)
     */
    public static int maximumProfitRecursive(int[] cost, int minShiftBreak,
            int maxShiftLength, Job[] jobs) {
        // IMPLEMENT THIS METHOD BY IMPLEMENTING THE PRIVATE METHOD IN THIS
        // CLASS THAT HAS THE SAME NAME
        return maximumProfitRecursive(cost, minShiftBreak, maxShiftLength, jobs,
                0, jobs.length, jobs.length);
    }

    /**
     * @require The array cost is not null, and all the integers in the array
     *          (the daily costs of the worker) are greater than or equal to
     *          zero. (The number of days that you are running the company is
     *          defined to be n = cost.length).
     * 
     *          The minimum number of days between shifts is greater than or
     *          equal to one (1 <= minShiftBreak). The maximum shift length is
     *          greater than or equal to one (1 <= maxShiftLength).
     * 
     *          The array jobs is not null, and does not contain null values.
     *          The jobs are sorted in ascending order of their end days. The
     *          end day of every job must be strictly less than the length of
     *          the cost array (n = cost.length).
     * 
     *          Additionally:
     * 
     *          (0 <= i <= jobs.length) and (0 <= j <= k <= jobs.length) and (if
     *          j != jobs.length then 0 <= j <= k < i)
     * 
     * @ensure Returns the maximum profit that can be earned by you for your
     *         company given that:
     * 
     *         (i): You can only select job opportunities from index i onwards
     *         in the list of jobs; and
     * 
     *         (ii) If j != jobs.length, then you cannot choose a job that
     *         starts earlier than day jobs[k].end() + 1; and
     * 
     *         (iii): If j != jobs.length, then you must select a shift that
     *         starts on day jobs[j].start(), and ends no earlier than end day
     *         jobs[k].end(). Since you must select a shift of this nature, you
     *         have an obligation to pay for it, and take it into consideration
     *         when you are selecting any further shifts to include.
     * 
     *         (See handout for details.)
     * 
     *         This method must be implemented using a recursive programming
     *         solution to the problem. It is expected to have a worst-case
     *         running time that is exponential. (You must NOT provide a dynamic
     *         programming solution to this question.)
     */
    private static int maximumProfitRecursive(int[] cost, int minShiftBreak,
            int maxShiftLength, Job[] jobs, int i, int j, int k) {

        if (i > jobs.length - 1) //no more jobs to check so go back up the recursion tree
            return 0;

        if (jobs[i].length() <= maxShiftLength) {
            if (j == jobs.length && k == jobs.length) { //havent added a job yet, or new shift has started
                return Math.max(jobs[i].payment() - costs(i, cost, jobs) +
                                maximumProfitRecursive(cost, minShiftBreak, maxShiftLength, jobs, i + 1, i, i),
                        maximumProfitRecursive(cost, minShiftBreak, maxShiftLength, jobs, i + 1, jobs.length, jobs.length));
            }
            if (jobs[i].compatible(jobs[k])) {  //check if the current job is compatabile with shift job k
                //if a break is possible between the last job and the current job its always optimum to take a break
                if ((jobs[i].start() - jobs[k].end()) > minShiftBreak)
                    return Math.max(jobs[i].payment() - costs(i, cost, jobs) +
                                    maximumProfitRecursive(cost, minShiftBreak, maxShiftLength, jobs, i + 1, i, i),
                            maximumProfitRecursive(cost, minShiftBreak, maxShiftLength, jobs, i + 1, j, k));

                //check if continuation of shift is possible (add job i to shift (j, k))
                Shift shift = new Shift(jobs[j].start(), jobs[i].end());
                if (shift.length() <= maxShiftLength) {//shift is possible
                    //must include losses of shift as well as losses between last job
                    return Math.max(jobs[i].payment() - losses(i, k, cost, jobs) - costs(i, cost, jobs) +
                                    maximumProfitRecursive(cost, minShiftBreak, maxShiftLength, jobs, i + 1, j, i),
                            maximumProfitRecursive(cost, minShiftBreak, maxShiftLength, jobs, i + 1, j, k));
                }
            }
        }//if the jobs are not compatible it will skip to the next job
        return maximumProfitRecursive(cost, minShiftBreak, maxShiftLength, jobs, i + 1, j, k);
    }

    /**
     * Calculates the cost of hiring a worker from a start day to an end day
     * @param start day is start day of job[n+1]
     * @param end day is the ending day of job[n]
     * @return the cost of hiring a worker on those days will be 0 if start day equals end day
     */
    public static int losses(int i, int k, int[] cost, Job[] jobs) {
        int start =  jobs[k].end() + 1;
        int end = jobs[i].start();
        int costs = 0;
        while (start < end) {
            costs = costs + cost[start++];
        }
        return costs;
    }

    /**
     * Calculates the costs of hiring a worker for a given job i
     * @param i job
     * @param cost list of day costs
     * @param jobs list of jobs
     * @return cost of hiring the worker from job.start to job.end
     */
    public static int costs(int i, int[] cost, Job[] jobs) {
        Shift payment = new Shift(jobs[i].start(), jobs[i].end());
        return payment.cost(cost);
    }
}
