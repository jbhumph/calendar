// John Humphrey
// 03/04/2024
// CS& 141
// Calendar 3

/* This program will perform a variety of functions of the users choice. 
It will print a calendar or current date, list events, input external events, or print a date to an external file. */

// For extra credit I made an additional menu option that allows a user to load in events from other external .txt files.


import java.util.Calendar;
import java.io.*;
import java.util.Scanner;
import java.util.*;

public class HJCalendar3 {
   public static void main(String[] args) 
         throws FileNotFoundException {
      Scanner input = new Scanner(System.in);
      
      int selected = 0;
      int selectedDay = 0;
      
      // Declare jagged array
      String events[][] = new String[12][];
      for (int i = 0; i < 12; i++) {
         events[i] = new String[getDays(i+1)];
      }
      
      // Scan for initial calendar events
      docScan("calendarEvents.txt", events);
      
      printCal();
      
      // While loop to call method for mainMenu until user quits program
      boolean x = true;
      while (x == true) {
         char answer = mainMenu();
         if (answer == 'v') {
            // Enter a new event and store in array
            System.out.println("Please enter an event (mm/dd event): ");
            String event = input.nextLine();
            int evMonth = extractMonth(event) - 1;
            int evDay = extractDay(event) - 1;
            String words = event.substring(6);
            events[evMonth][evDay] = words;
            System.out.println(events[1][7]);
         } else if (answer == 'l') {
            // Load in an external documents events
            System.out.println("Each line of your .txt document must be in the format \"mm/dd Event_Name\"");
            int tF = 0;
            String doc = "No input";
            while (tF == 0) {
               System.out.println("Please enter a valid file name / path: ");
               doc = input.nextLine();
               tF = docScan(doc, events);
            }
            System.out.println(doc + " provided " + tF + " new events.\n");
         } else if (answer == 'f') {
            // Print a calendar to an external document
            System.out.print("Please enter an output file name: ");
            String outputName = input.nextLine();
            System.out.print("Please enter a date to print: ");
            String date = input.nextLine();
            int month = extractMonth(date);
            int day = extractDay(date);
            print(outputName, month, day, events);
         } else if (answer == 'e') {
            // Enter a date and print the corresponding calendar
            System.out.println("What date would you like to see? (mm/dd)");
            String date = input.nextLine();
            int month = extractMonth(date);
            int day = extractDay(date);
            selected = month;
            selectedDay = day;
            System.out.println("\nREQUESTED DATE:");
            printMonth(month, day, events, System.out);
            printDate(month, day, System.out);
         } else if (answer == 't') {
            // get today's date and display calendar
            Calendar today = Calendar.getInstance();
            int tmon = today.get(Calendar.MONTH) + 1;
            int tday = today.get(Calendar.DATE);
            selected = tmon;
            selectedDay = tday;
            System.out.println("\nTODAY'S DATE:");
            printMonth(tmon, tday, events, System.out);
            printDate(tmon, tday, System.out);
         } else if (answer == 'n') {
            // Display next month
            if (selected == 0) {
               System.out.println("You must have a calendar displayed first.");
            } else if (selected == 12) {
               printMonth(1, 0, events, System.out);
               printDate(1, selectedDay, System.out);
               selected = 1;
            } else {
               printMonth(selected + 1, 0, events, System.out);
               printDate(selected + 1, selectedDay, System.out);  
               selected += 1;
            }
         } else if (answer == 'p') {
            // Display previous month 
            if (selected == 0) {
               System.out.println("You must have a calendar displayed first.");
            } else if (selected == 1) {
               printMonth(12, 0, events, System.out);
               printDate(12, selectedDay, System.out);
               selected = 12;
            } else {
               printMonth(selected - 1, 0, events, System.out);
               printDate(selected - 1, selectedDay, System.out);
               selected -= 1;
            }
         } else if (answer == 'q') {
            // Quit program
            System.out.print("Goodbye");
            x = false;
         } else {
            System.out.print("Please enter a valid command");
         }
      }
   } // end method main
   
   public static int docScan(String fileName, String event[][]) 
         throws FileNotFoundException {
      // This method will scan in calendar dates from an external .txt file
      File i = new File(fileName);
      int count = 0;
      if (i.exists()) {
         Scanner file = new Scanner(new File(fileName));
         while (file.hasNextLine()) {
            String line = file.nextLine();
            int Month = extractMonth(line) - 1;
            int Day = extractDay(line) - 1;
            String words = line.substring(6);
            event[Month][Day] = words;
            count++;
         }
      } else {
         return 0;
      }
      return count;
   } // end method docScan
   
   public static void print(String name, int month, int day, String event[][]) 
         throws FileNotFoundException {
      // prints a chosen calendar to an external file
      PrintStream output = new PrintStream(new File(name));
      printMonth(month, day, event, output);
      printDate(month, day, output);
      output.close();
   } // end method print
   
   public static char mainMenu() {
      // prints main menu, prompts user for input, returns char as answer
      Scanner input = new Scanner(System.in);
      
      System.out.println("Please type a command:");
      System.out.println("    \"e\"  to enter a date and display the corresponding calendar.");
      System.out.println("    \"t\"  to get today's date and display today's calendar.");
      System.out.println("    \"n\"  to display the next month.");
      System.out.println("    \"p\"  to display the previous month.");
      System.out.println("    \"ev\" to enter an event manually");
      System.out.println("    \"l\"  to load events from an external .txt file");
      System.out.println("    \"fp\" to print a calendar to an external file.");
      System.out.println("    \"q\"  to quit the program.");
      
      String answer = input.nextLine();
      int x = answer.length() - 1;
      char a = answer.charAt(x);
      if (a == 'p' && x == 1) {
         a = 'f';
      }
      
      return a;
   } // end method mainMenu
   
   public static void printMonth(int month, int target, String events[][], PrintStream output) {
      // Prints out a months calendar
      int days = getDays(month);
      int dayOf = weekDay(month);
      int weeks = (days + dayOf) / 7;
      int day = 1;
      
      printCal2(month, days, output);
      
      output.printf("                                   %d\n", month);
      output.println("  SUNDAY    MONDAY    TUESDAY  WEDNESDAY  THURSDAY   FRIDAY  SATURDAY  ");
      int i = 0;
      // Iterates through each row of the month until last day is reached
      while (day <= days) {
         day = printRow2(i, day, days, dayOf, weeks, target, month, events, output);
         i++;
      }
      output.println("\n");
   }  // end method printMonth
   
   public static int printRow2(int row, int day, int days, int dayOf, int last, int target, int m, String e[][], PrintStream output) {
      // Prints a single row in a month, composed of several sub-rows
      boolean end = false;
      int stop = 0;
      int mark = 9;
      int evCheck = day - 1;
      
      // subrow 0 (header)
      if (row == 0) {
         for (int i = 0; i < 7; i++) {
            if (i < dayOf) {
               output.print("          ");
            } else if (dayOf == 6) {
               output.print("+=========+");
            } else if (i == dayOf) {
               output.print("+=========");
            } else if (i == 6) {
               output.print("==========+");
            }else {
               output.print("==========");
            } 
         }
         output.print("\n");
      } else {
         output.println("|---------------------------------------------------------------------|");
      }
      
      // subrow 1
      if (row == 0) {
         // if this is the first row, print date or...
         for (int i = 0; i < 7; i++) {
            if (i < dayOf) {
               output.print("          ");
            } else if (day > days) {
               output.print("          ");
            } else if (day < 10) {
               output.printf("|%d        ", day);
               if (day == target) {
                  mark = i;
               }
               day++;
               if (i == 6) {
                  output.print("|\n");
               }
            } else {
               output.printf("|%d       ", day);
               if (day == target) {
                  mark = i;
               }
               day++;
            }
         }
      } else {
         // cycle through each cell to print date
         for (int i = 0; i < 7; i++) {
            if (day < 10) {
               output.printf("|%d        ", day);
               if (i == 6) {
                  output.print("|");
               }
               if (day == target) {
                  mark = i;
               }
               day++;
            } else if (day == days) {
               output.printf("|%d       |", day);
               end = true;
               stop = i;
               if (day == target) {
                  mark = i;
               }
               day++;
            } else if (day > days) {
               output.print("          ");
            } else {
               output.printf("|%d       ", day);
               if (i == 6) {
                  output.print("|");
               }
               if (day == target) {
                  mark = i;
               }
               day++;
            }
         }
         output.print("\n");
      }
      // subrow 2
      // print each cell and mark if selected date
      char d = ' ';
      if (row == 0) {
         for (int i = 0; i < 7; i++) {
            if (mark == i) {
               d = '@';
            } else {
               d = ' ';
            }
            if (i < dayOf) {
               output.print("     "+d+"    ");
            } else {
               output.print("|    "+d+"    ");
            }
         }
         output.println("|");
      } else if (end == true){
         for (int i = 0; i < 7; i++) {
            if (mark == i) {
               d = '@';
            } else {
               d = ' ';
            }
            if (i == stop) {
               output.print("|    "+d+"    |");
            } else if (i > stop) {
               output.print("     "+d+"    ");
            } else {
               output.print("|    "+d+"    ");
            }
         }
         output.print("\n");
      } else {
         for (int i = 0; i < 7; i++) {
            if (mark == i) {
               d = '@';
            } else {
               d = ' ';
            }
            if (day == days) {
               output.print("|    "+d+"    ");
            } else if (day > days) {
               output.print("     "+d+"    ");
            } else {
               output.print("|    "+d+"    ");
            }
         }
         output.println("|");
      }
      mark = 9;
      
      // subrow 3
      // print each cell
      String event = "         ";
      if (row == 0) {
         for (int i = 0; i < 7; i++) {
            event = events(m - 1, evCheck, e);
            if (i < dayOf) {
               output.print("          ");
            } else {
               output.print("|" + event);
               evCheck++;
            }
         }
         output.println("|");
         
      } else if (end == true){
         for (int i = 0; i < 7; i++) {
            event = events(m - 1, evCheck, e);
            if (i == stop) {
               output.print("|"+ event + "|");
            } else if (i > stop) {
               output.print("          ");
            } else {
               output.print("|" + event);
               evCheck++;
            }
         }
         output.print("\n");
         
      } else {
         for (int i = 0; i < 7; i++) {
            event = events(m - 1, evCheck + i, e);
            if (day == days) {
               output.print("|" + event);
            } else if (day > days) {
               output.print(event + " ");
            } else {
               output.print("|" + event);
            }
         }
         output.println("|");
      }
      // subrow 4
      // print each cell
      if (row == 0) {
         for (int i = 0; i < 7; i++) {
            if (i < dayOf) {
               output.print("          ");
            } else {
               output.print("|         ");
            }
         }
         output.println("|");
         
      } else if (end == true){
         for (int i = 0; i < 7; i++) {
            if (i == stop) {
               output.print("|         |");
            } else if (i > stop) {
               output.print("          ");
            } else {
               output.print("|         ");
            }
         }
         output.print("\n");
         
      } else {
         for (int i = 0; i < 7; i++) {
            if (day == days) {
               output.print("|         ");
            } else if (day > days) {
               output.print("          ");
            } else {
               output.print("|         ");
            }
         }
         output.println("|");
      }

      while (end == true) {
         // print footer for last line
         for (int i = 0; i < 7; i++) {
            if (i == 0 && i == stop) {
               output.print("+=========+");
            } else if (i == 0) {
               output.print("+=========");
            } else if (i > stop) {
               output.print("          ");
            } else if (i == stop) {
               output.print("==========+");
            }else {
               output.print("==========");
            } 
         }
         return day;
      }
      return day;
   } // end method printRow2
   
   public static String events(int month, int day, String event[][]) {
      // Determines what days event to return to calendar
      if (event[month][day] == null) {
         return "         ";
      } else {
         if (event[month][day].length() > 9) { 
            return event[month][day].substring(0, 8) + "*";
         } else if (event[month][day].length() < 9) {
            int space = 9 - event[month][day].length();
            String word = event[month][day];
            for (int i = 0; i < space; i++) {
               word += " "; 
            }
            return word;
         } else {
            return event[month][day];
         }
      }
   } // end method events
   
   public static int weekDay(int month) {
   // Determines what day of the week the month begins on
   switch (month) {
         case 1:
            return 0;
         case 2:
            return 3;
         case 3:
            return 3;
         case 4:
            return 6;
         case 5:
            return 1;
         case 6:
            return 4;
         case 7:
            return 6;
         case 8:
            return 2;
         case 9:
            return 5;
         case 10:
            return 0;
         case 11:
            return 3;
         case 12:
            return 5;
      }
      return 0;
   } // end method weekDay
   
   public static int getDays(int month) {
      // Returns the number of days in a given month
      switch (month) {
         case 1:
            return 31;
         case 2:
            return 28;
         case 3:
            return 31;
         case 4:
            return 30;
         case 5:
            return 31;
         case 6:
            return 30;
         case 7:
            return 31;
         case 8:
            return 31;
         case 9:
            return 30;
         case 10:
            return 31;
         case 11:
            return 30;
         case 12:
            return 31;
      }
      return 0;
   } // end method getDays
      
   public static void printDate(int month, int day, PrintStream output) {
      // prints the input or current month and date below calendar
      output.printf("Month: %d\n", month);
      output.printf("Day: %d\n", day);
   }  // end method printDate
   
   public static int extractMonth(String date) {
      // seperates out just the month from user input
      String sub = date.substring(0, 2);
      int month = Integer.parseInt(sub);
      return month;
   }  // end method extractMonth
   
   public static int extractDay(String date) {
      // seperates out just the day from user input
      String sub = date.substring(3, 5);
      int day = Integer.parseInt(sub);
      return day;
   }  // end method extractDay
   
   public static void printCal2(int month, int day, PrintStream output) {
      // prints an evolving calendar for each month of the year 
      int x = 33 / month;
      int y = x - 1;
      
      // line start
      for (int i = 0; i < 71; i++) {
         output.print("*");
      }
      // line 0
      output.print("\n");
      output.print("*");
      for (int i = 0; i < 69; i++) {
         output.print(" ");
      }
      output.print("*");
      
      // line 1
      output.print("\n");
      output.print("*");
      for (int i = 0; i < month; i++) {
         for (int j = 0; j < x-1; j++) {
            output.print(" "); 
         }
         output.print("\\");
      }
      if (month == 12) {
         output.print("         ");
      } else if (month == 10 || month == 6 || month == 5) {
         output.print("   ");
      } else if (month == 9) {
         output.print("      ");
      } else if (month == 8 || month == 4 || month == 2) {
         output.print(" ");
      } else if (month == 7) {
         output.print("     ");
      }
      output.print(" | ");
      if (month == 12) {
         output.print("         ");
      } else if (month == 10 || month == 6 || month == 5) {
         output.print("   ");
      } else if (month == 9) {
         output.print("      ");
      } else if (month == 8 || month == 4 || month == 2) {
         output.print(" ");
      } else if (month == 7) {
         output.print("     ");
      }
      for (int i = 0; i < month; i++) {
         output.print("/");
         for (int j = 0; j < x-1; j++) {
            output.print(" "); 
         }
      }
      output.print("*");
      
      // line 2
      output.print("\n");
      output.print("*");
      for (int i = 0; i < 33; i++) {
         output.print(" ");
      }
      output.print("@@@");
      for (int i = 0; i < 33; i++) {
         output.print(" ");
      }
      output.print("*");
      // line 3
      output.print("\n");
      output.print("*");
      for (int i = 0; i < 33; i++) {
         output.print(" ");
      }
      output.print("@@@");
      for (int i = 0; i < 33; i++) {
         output.print(" ");
      }
      output.print("*");

      // line 4
      output.print("\n");
      output.print("*");
      for (int i = 0; i < month; i++) {
         for (int j = 0; j < x-1; j++) {
            output.print(" "); 
         }
         output.print("/");
      }
      if (month == 12) {
         output.print("         ");
      } else if (month == 10 || month == 6 || month == 5) {
         output.print("   ");
      } else if (month == 9) {
         output.print("      ");
      } else if (month == 8 || month == 4 || month == 2) {
         output.print(" ");
      } else if (month == 7) {
         output.print("     ");
      }
      output.print(" | ");
      if (month == 12) {
         output.print("         ");
      } else if (month == 10 || month == 6 || month == 5) {
         output.print("   ");
      } else if (month == 9) {
         output.print("      ");
      } else if (month == 8 || month == 4 || month == 2) {
         output.print(" ");
      } else if (month == 7) {
         output.print("     ");
      }
      for (int i = 0; i < month; i++) {
         output.print("\\");
         for (int j = 0; j < x-1; j++) {
            output.print(" "); 
         }
      }
      output.print("*");
      
      // line 0
      output.print("\n");
      output.print("*");
      for (int i = 0; i < 69; i++) {
         output.print(" ");
      }
      output.print("*");
      
      // line end
      
      output.print("\n");
      for (int i = 0; i < 71; i++) {
         output.print("*");
      }
      output.print("\n\n");
   
   } // end method printCal2 
   
   public static void printCal() {
      // prints ASCII artwork
      for (int i = 0; i < 71; i++) {
         System.out.print("*");
      }
      System.out.print("\n");
      System.out.println("*                                                                     *");
      System.out.println("*                                                                     *");
      System.out.println("*  |--\\  |---| |--- | /   |---| |\\  |   |    | |---| |--\\  |    |-\\   *");
      System.out.println("*  |---\\ |   | |    |/    |   | | \\ |   | /\\ | |   | |---\\ |    |  |  *");
      System.out.println("*  |  \\  |___| |___ | \\   |___| |  \\|   |/  \\| |___| |  \\  |___ |_/   *");
      System.out.println("*                                                                     *");
      System.out.println("*                                                                     *");
      System.out.println("*              |  |             __________                            *");
      System.out.println("*              ||||           /\\          \\                           *");
      System.out.println("*             \\              /  \\          \\                          *");
      System.out.println("*              |  |         /  o \\          \\                         *");
      System.out.println("*              |  |        /      \\__________\\                        *");
      System.out.println("*              |  |        \\      /          /                        *");
      System.out.println("*               \\  \\        \\    /  @    @  /                         *");
      System.out.println("*                \\  \\       /\\  /     >    /                          *");
      System.out.println("*                 \\  \\     /  \\/__________/                           *");
      System.out.println("*                  \\  \\___/    ==========_                            *");
      System.out.println("*                   \\                     \\                           *");
      System.out.println("*                    \\___                  \\                          *");
      System.out.println("*                        |             |\\   \\                         *");
      System.out.println("*                        |             | \\   \\                        *");
      System.out.println("*                                         \\   \\                       *");
      
      for (int i = 0; i < 71; i++) {
         System.out.print("*");
      }
      System.out.print("\n\n");
      System.out.println("                               CALENDAR\n");

   } // end method printCal

} // end class HJCalendar1