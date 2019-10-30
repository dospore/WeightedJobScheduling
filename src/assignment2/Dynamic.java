package assignment2;

import java.util.*;


public class Dynamic {

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
     *         This method must be implemented using an efficient bottom-up
     *         dynamic programming solution to the problem (not memoised).
     */
    public static int maximumProfitDynamic(int[] cost, int minShiftBreak,
            int maxShiftLength, Job[] jobs) {
        int j, k, profit;
        int K[][][] = new int[jobs.length][jobs.length][jobs.length];//create 3d array for i,j,k
        //for each job, find the maximum shift extending from that job in terms of j and k. Store this shifts profit
        //at i, j, k
        for (int p = 0; p < jobs.length; p++) {
            j = k = jobs.length; //set j and k to be jobs.length
            for (int i = p; i < jobs.length; i++) {
                if (jobs[i].length() > maxShiftLength) {
                    break;//if its not greater than maxShift.length its not possible
                } else if (j == jobs.length && k == jobs.length) {
                    //no jobs have been added yet
                    profit = jobs[i].payment() - costs(i, cost, jobs);
                    K[i][i][i] = profit;
                    j = k = i; //set j and k to be i since this is the first job in the shift
                } else if (jobs[i].compatible(jobs[k])) { //check if the next job is compatible with current shift
                    Shift shift = new Shift(jobs[j].start(), jobs[i].end());
                    if ((jobs[i].start() - jobs[k].end()) > minShiftBreak) {//check if a break can be made
                        Shift ending = new Shift(jobs[j].start(), jobs[k].end());
                        profit = jobs[i].payment() - costs(i, cost, jobs);
                        //end the current shift and start a new shift. Continue on the profit from the previous shifts
                        K[i][i][i] = Math.max(profit + K[i-1][j][k], K[i-1][j][k]);
                        k = j = i;//set the current new shift to job i
                    } else if (shift.length() <= maxShiftLength) {//shift is possible
                        profit = jobs[i].payment() - losses(i, k, cost, jobs) - costs(i, cost, jobs);
                        //j stays the same as we are continueing the shift
                        K[i][j][i] = Math.max(profit + K[k][j][k], K[k][j][k]);
                        k = i;
                    }
                    //shift is not possible need to take a break
                }
                //else skip the job
            }
        }

        ArrayList<ShiftInfo> maxShifts = maximumSubProblems(getMaxSubProblems(K, jobs, minShiftBreak), minShiftBreak);
        if (maxShifts == null)
            return 0;

        return getProfit(maxShifts);
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
     * @ensure Returns a valid selection of shifts and job opportunities that
     *         results in the largest possible profit to your company (given
     *         parameters cost, minShiftBreak, maxShiftLength and jobs).
     * 
     *         (See handout for details.)
     * 
     *         This method must be implemented using an efficient bottom-up
     *         dynamic programming solution to the problem (not memoised).
     */
    public static Solution optimalSolutionDynamic(int[] cost, int minShiftBreak,
            int maxShiftLength, Job[] jobs) {
        int j, k, profit;
        int K[][][] = new int[jobs.length][jobs.length][jobs.length];//create 3d array for i,j,k
        //for each job, find the maximum shift extending from that job in terms of j and k. Store this shifts profit
        //at i, j, k
        for (int p = 0; p < jobs.length; p++) {
            j = k = jobs.length; //set j and k to be jobs.length
            for (int i = p; i < jobs.length; i++) {
                if (jobs[i].length() > maxShiftLength) {
                    break;//if its not greater than maxShift.length its not possible
                } else if (j == jobs.length && k == jobs.length) {
                    //no jobs have been added yet
                    profit = jobs[i].payment() - costs(i, cost, jobs);
                    K[i][i][i] = profit;
                    j = k = i; //set j and k to be i since this is the first job in the shift
                } else if (jobs[i].compatible(jobs[k])) { //check if the next job is compatible with current shift
                    Shift shift = new Shift(jobs[j].start(), jobs[i].end());
                    if ((jobs[i].start() - jobs[k].end()) > minShiftBreak) {//check if a break can be made
                        Shift ending = new Shift(jobs[j].start(), jobs[k].end());
                        profit = jobs[i].payment() - costs(i, cost, jobs);
                        //end the current shift and start a new shift. Continue on the profit from the previous shifts
                        K[i][i][i] = Math.max(profit + K[i-1][j][k], K[i-1][j][k]);
                        k = j = i;//set the current new shift to job i
                    } else if (shift.length() <= maxShiftLength) {//shift is possible
                        profit = jobs[i].payment() - losses(i, k, cost, jobs) - costs(i, cost, jobs);
                        //j stays the same as we are continueing the shift
                        K[i][j][i] = Math.max(profit + K[k][j][k], K[k][j][k]);
                        k = i;
                    }
                    //shift is not possible need to take a break
                }
                //else skip the job
            }
        }

        ArrayList<ShiftInfo> maxShifts = maximumSubProblems(getMaxSubProblems(K, jobs, minShiftBreak), minShiftBreak);
        if (maxShifts == null)
            return new Solution(new ArrayList<Shift>(), new ArrayList<Job>());
        return new Solution(getShifts(maxShifts), getJobs(maxShifts));
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

    /**
     * Returns an array list containing the maximum shift for each job (ie max shift starting from each job)
     * @param K 3d array containing all combinations of shifts
     * @param jobs list of jobs
     * @param minShiftBreak
     * @return
     */
    public static ShiftInfo[] getMaxSubProblems(int[][][] K, Job[] jobs, int minShiftBreak) {
        //Each i, i, i of the 3d array will contain the profit of completing that single job
        //As j decreases the shift length will increase. ie i, i-1, i will be a longer shift
        //So look for the maximum shift for each job
        int k, max;
        int len = jobs.length;
        ShiftInfo[] shifts = new ShiftInfo[len];
        for (int i = len - 1; i >= 0; i--) {
            k = i;
            max = K[i][i][i];
            shifts[i] = new ShiftInfo(new Shift(jobs[i].start(), jobs[i].end()), max, getShiftJobs(jobs, i, i));
            for (int j = i; j >= 0; j--) {
                if (max < K[i][j][k]) {
                    //Set the new max and create a shift a new shift
                    max = K[i][j][k];
                    shifts[i] = new ShiftInfo(new Shift(jobs[j].start(), jobs[k].end()), max, getShiftJobs(jobs, j, k));
                }
            }
        }
        Arrays.sort(shifts, new ShiftComparator()); //ensure the shifts are sorted in finishing time
        return shifts;
    }

    /**
     * @Require Sorted shifts by finish time
     * For every shift find the first job which does not overlap with this shift
     * and see if this shift profit plus profit till last non overlapping shift is greater
     * than profit till last shift. This function is based on regular job allocation problem using dynamic programming.
     * This is because I had shifts and profits but required to find the maximum compatible shifts.
     * Requires a new data type to store, shift, profit and the jobs within that shift
     * @param shifts
     * @param minShiftBreak minimum break between shifts (used for compatibility test)
     * @return list of shifts
     */
    public static ArrayList<ShiftInfo> maximumSubProblems(ShiftInfo[] shifts, int minShiftBreak) {
        ArrayList<ShiftInfo> T[] = new ArrayList[shifts.length];
        T[0] = new ArrayList<ShiftInfo>(); //initialise T[0] to the first element in shifts
        T[0].add(shifts[0]);
        for(int i=1; i < shifts.length; i++){
            T[i] = new ArrayList<ShiftInfo>();
            if (shifts[i].profit > getProfit(T[i-1])) //replace T[i] with T[i-1] if T[i-1] > shifts[i] profit
                T[i].add(shifts[i]);
            else
                T[i].addAll(T[i-1]);
            for(int j=i-1; j >=0; j--) {//loop back through the jobs and attempt to increase the maximum at each index
                if(shifts[j].shift.compatible(shifts[i].shift, minShiftBreak)){
                    if (shifts[i].profit + getProfit(T[j]) > getProfit(T[i])) {
                        //if we need to increase the maximum then clear the current array at i and replace it
                        //this is so the shifts and their correlated jobs are kept track of for the final solution
                        T[i].clear();
                        T[i].addAll(T[j]);
                        T[i].add(shifts[i]);
                    }//else do nothing
                    break;
                }
            }
        }
        int max = 0;//find the index of the max value
        for (int i = 1; i < T.length; i++)
            if (getProfit(T[i]) > getProfit(T[max]))
                max = i;

        //if it is 0 then no jobs were compatible. This stems from the initial construction
        //the 3d array
        if (getProfit(T[max]) == 0)
            return null;

        return T[max];
    }

    /**
     * Get the profit of a list of shifts
     * @param maxShifts list of shiftsinfo
     * @return the total profit of combining the given shifts
     */
    public static int getProfit(ArrayList<ShiftInfo> maxShifts) {
        int sum = 0;
        for (ShiftInfo shift : maxShifts) {
            sum += shift.profit;
        }
        return sum;
    }

    /**
     * Returns the jobs correlating to a list of shifts
     * @param maxShifts list of shiftsinfo
     * @return list of jobs correlating to the given shifts
     */
    public static List<Job> getJobs(ArrayList<ShiftInfo> maxShifts) {
        List<Job> jobs = new ArrayList<Job>();
        for (ShiftInfo shift : maxShifts) {
            jobs.addAll(shift.jobs);
        }
        return jobs;
    }

    /**
     * Returns the jobs correlating to a list of shiftsinfo
     * @param maxShifts list of shiftsinfo
     * @return list of shifts correlating to the given shiftsinfo
     */
    public static List<Shift> getShifts(ArrayList<ShiftInfo> maxShifts) {
        List<Shift> shifts = new ArrayList<Shift>();
        for (ShiftInfo shift : maxShifts) {
            shifts.add(shift.shift);
        }
        return shifts;
    }

    /**
     * Determines which job are included within a given shift. As long as the jobs within the shift are compatible with
     * both j and k, and start and finish time are within j.start and k.end the job will be included in the shift.
     * @param jobs
     * @param j is the first job in the shift
     * @param k is the last job in the shift
     * @return a list of jobs included in the shift
     */
    public static ArrayList<Job> getShiftJobs(Job[] jobs, int j, int k) {
        ArrayList<Job> shiftJobs = new ArrayList<Job>();
        int startJob = j;
        shiftJobs.add(jobs[j]);
        int endJob = k;
        int i = startJob + 1;
        while (startJob <= endJob && i < k) {
            if (jobs[startJob].compatible(jobs[i]) && jobs[endJob].compatible(jobs[i])) {
                shiftJobs.add(jobs[i]);
            }
            startJob++;
            i++;
        }
        if (j!= k) {//dont want to add duplicate
            shiftJobs.add(jobs[k]);
        }
        return shiftJobs;
    }

    /**
     * Class used so I could store shift info and map a shift to a profit and a list of jobs. I would put it in
     * a struct but java uses classes.
     */
    private static class ShiftInfo {
        Shift shift;
        int profit;
        ArrayList<Job> jobs;

        ShiftInfo (Shift shift, int profit, ArrayList<Job> jobs) {
            this.shift = shift;
            this.profit = profit;
            this.jobs = jobs;
        }

        public int end() { return this.shift.end(); }

        public int start() { return this.shift.start(); }

        @Override
        public String toString() {
            return String.format("%s its profit %d and its jobs %s", shift, profit, jobs);
        }
    }

    // Used to sort shifts according to finish times
    private static class ShiftComparator implements Comparator<ShiftInfo> {
        public int compare(ShiftInfo a, ShiftInfo b) {
            if (a == null || b == null)
                return -1;
            else if (a.shift.end() < b.shift.end())
                return -1;
            else if (a.shift.end() == b.shift.end())
                return 0;
            else
                return 1;
        }
    }
}



