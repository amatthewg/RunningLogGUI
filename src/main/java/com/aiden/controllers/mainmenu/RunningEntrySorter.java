package com.aiden.controllers.mainmenu;

import com.aiden.misc.RunningEntry;

import java.util.List;

// Class responsible for sorting running entries by name,
// distance, time, or pace. Can reverse the list if necessary
public class RunningEntrySorter {



    public RunningEntrySorter() {
    }

    public void sortByNameAlphabetical(List<RunningEntry> inputList, boolean reverse) {
        if(inputList == null || inputList.isEmpty()) return;
        int low = 0;
        int high = inputList.size() - 1;
        quicksortString(inputList, low, high, reverse);
    }
    public void sortByDistanceDescending(List<RunningEntry> inputList, boolean reverse) {
        if(inputList == null || inputList.isEmpty()) return;
        int low = 0;
        int high = inputList.size() - 1;
        quicksortDistance(inputList, low, high, reverse);
    }
    public void sortByTimeAscending(List<RunningEntry> inputList, boolean reverse) {
        if(inputList == null || inputList.isEmpty()) return;
        int low = 0;
        int high = inputList.size() - 1;
        quicksortTime(inputList, low, high, reverse);
    }
    public void sortByPaceAscending(List<RunningEntry> inputList, boolean reverse) {
        if(inputList == null || inputList.isEmpty()) return;
        int low = 0;
        int high = inputList.size() - 1;
        quicksortPace(inputList, low, high, reverse);
    }




    // Helpers for sorting by String
    private static void quicksortString(List<RunningEntry> inputList, int low, int high, boolean reverse) {
        if(low < high) {
            int pivotIndex = partitionString(inputList, low, high, reverse);
            quicksortString(inputList, low, pivotIndex-1, reverse);
            quicksortString(inputList, pivotIndex+1, high, reverse);
        }
    }
    private static int partitionString(List<RunningEntry> inputList, int low, int high, boolean reverse) {
        String pivot = inputList.get(high).getRunnerName();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            String currentName = inputList.get(j).getRunnerName();
            int comparisonResult = currentName.compareTo(pivot);
            if (reverse) comparisonResult = -comparisonResult;
            if (comparisonResult <= 0) {
                i++;
                swap(inputList, i, j);
            }
        }
        swap(inputList, i + 1, high);
        return i + 1;
    }
    // Helpers for sorting by Distance
    private static void quicksortDistance(List<RunningEntry> inputList, int low, int high, boolean reverse) {
        if (low < high) {
            int pivotIndex = partitionDistance(inputList, low, high, !reverse);
            quicksortDistance(inputList, low, pivotIndex - 1, reverse);
            quicksortDistance(inputList, pivotIndex + 1, high, reverse);
        }
    }
    private static int partitionDistance(List<RunningEntry> inputList, int low, int high, boolean reverse) {
        double pivot = inputList.get(high).getDistance();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            double currentDistance = inputList.get(j).getDistance();
            int comparisonResult = Double.compare(currentDistance, pivot);

            // If reverse is true, swap elements for descending order
            if (reverse) {
                comparisonResult = -comparisonResult;
            }

            if (comparisonResult <= 0) {
                i++;
                swap(inputList, i, j);
            }
        }

        swap(inputList, i + 1, high);
        return i + 1;
    }

    // Helpers for sorting by Time
    private static void quicksortTime(List<RunningEntry> inputList, int low, int high, boolean reverse) {
        if (low < high) {
            int pivotIndex = partitionTime(inputList, low, high, reverse);
            quicksortTime(inputList, low, pivotIndex - 1, reverse);
            quicksortTime(inputList, pivotIndex + 1, high, reverse);
        }
    }
    private static int partitionTime(List<RunningEntry> inputList, int low, int high, boolean reverse) {
        double pivot = inputList.get(high).getTime();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            double currentTime = inputList.get(j).getTime();
            int comparisonResult = Double.compare(currentTime, pivot);
            if (reverse) {
                comparisonResult = -comparisonResult;
            }
            if (comparisonResult <= 0) {
                i++;
                swap(inputList, i, j);
            }
        }

        swap(inputList, i + 1, high);
        return i + 1;
    }


    // Helpers for sorting by Pace
    private static void quicksortPace(List<RunningEntry> inputList, int low, int high, boolean reverse) {
        if (low < high) {
            int pivotIndex = partitionPace(inputList, low, high, reverse);
            quicksortPace(inputList, low, pivotIndex - 1, reverse);
            quicksortPace(inputList, pivotIndex + 1, high, reverse);
        }
    }
    private static int partitionPace(List<RunningEntry> inputList, int low, int high, boolean reverse) {
        double pivot = inputList.get(high).getPace();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            double currentPace = inputList.get(j).getPace();
            int comparisonResult = Double.compare(currentPace, pivot);
            if (reverse) {
                comparisonResult = -comparisonResult;
            }
            if (comparisonResult <= 0) {
                i++;
                swap(inputList, i, j);
            }
        }

        swap(inputList, i + 1, high);
        return i + 1;
    }


    // Swap method used by all sorters
    private static void swap(List<RunningEntry> inputList, int i, int j) {
        RunningEntry temp = inputList.get(i);
        inputList.set(i, inputList.get(j));
        inputList.set(j, temp);
    }

}
