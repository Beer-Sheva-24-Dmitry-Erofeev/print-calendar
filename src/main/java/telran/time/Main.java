package telran.time;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

record MonthYear(int month, int year, int weekStartsOnDay) {

}

public class Main {

    public static void main(String[] args) {
        MonthYear monthYear;
        try {
            monthYear = getMonthYear(args);
            printCalendar(monthYear);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static MonthYear getMonthYear(String[] args) throws Exception {
        // Setting the defaults
        // Может это можно сделать перегрузкой метода, но я не знаю как
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        int weekStartsOnDay = DayOfWeek.MONDAY.getValue();

        // Convert args to int[] if they exist
        if (args.length > 0) {
            int[] input = new int[args.length];
            for (int i = 0; i < args.length; i++) {
                input[i] = Integer.parseInt(args[i]);
            }

            // Set month if the first argument is valid
            if (input.length >= 1 && input[0] >= 1 && input[0] <= 12) {
                month = input[0];
            } else if (input.length >= 1) {
                throw new Exception("Invalid month number, correct is 1-12");
            }

            // Set year if the second argument is valid
            if (input.length >= 2 && input[1] >= 0) {
                year = input[1];
            } else if (input.length >= 2) {
                throw new Exception("Invalid year number, correct is >= 0");
            }

            // Set week start day if the third argument is valid
            if (input.length == 3 && input[2] >= 1 && input[2] <= 7) {
                weekStartsOnDay = input[2];
            } else if (input.length == 3) {
                throw new Exception("Invalid starting day number, correct is 1-7");
            }

            // Too many arguments
            if (input.length > 3) {
                throw new Exception("There should be no more than 3 arguments");
            }
        }
        return new MonthYear(month, year, weekStartsOnDay);
    }

    private static void printCalendar(MonthYear monthYear) {
        printTitle(monthYear);
        printWeekDays(monthYear);
        printDates(monthYear);

    }

    private static void printTitle(MonthYear monthYear) {
        String getMonthName = Month
                .of(monthYear.month())
                .getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault());
        String getYearName = Integer.toString(monthYear.year());
        System.out.print("\n\t\t" + getYearName + "\t" + getMonthName + "\n");
    }

    private static void printWeekDays(MonthYear monthYear) {
        DayOfWeek[] daysOfWeek = DayOfWeek.values();
        int startDay = monthYear.weekStartsOnDay();
        String res = "";
        for (int i = startDay - 1; i < startDay + 6; i++) {
            res += daysOfWeek[i % 7]
                    .getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    + "\t";
        }
        System.out.print(res + "\n");
    }

    private static void printDates(MonthYear monthYear) {
        int firstDay = getFirstDayOfWeek(monthYear);
        int weekStartDay = monthYear.weekStartsOnDay();
        int totalDays = getLastDayOfMonth(monthYear);
        int offset = getOffset(firstDay, weekStartDay);
        String res = "";
        for (int i = 0; i < offset; i++) {
            res += "\t";
        }
        for (int day = 1; day <= totalDays; day++) {
            res += day + "\t";
            if ((day + offset) % 7 == 0) {
                res += "\n";
            }
        }
        System.out.print(res + "\n\n");
        // printf("%4d", number) - не знаю как использовать эту подсказку, увы
    }

    private static int getFirstDayOfWeek(MonthYear monthYear) {
        LocalDate tmp = LocalDate.of(monthYear.year(), monthYear.month(), 1);
        return tmp.getDayOfWeek().getValue();
    }

    private static int getOffset(int firstWeekDay, int weekStartDay) {
        return (firstWeekDay - weekStartDay + 7) % 7;
    }

    private static int getLastDayOfMonth(MonthYear monthYear) {
        LocalDate tmp = LocalDate.of(monthYear.year(), monthYear.month(), 1);
        return tmp.lengthOfMonth();
    }
}
