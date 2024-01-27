package com.naveen;

import java.awt.print.Book;
import java.util.*;

class Point
{
    char point;
    int distance;

    public char getPoint() {
        return point;
    }


    Point(char point,int distance)
    {
        this.point = point;
        this.distance = distance;
    }
}

class Taxi
{
    String taxiName;
    char taxiLocation;
    int totalAmt;
    int availableFrom;

    public String getTaxiName() {
        return taxiName;
    }

    public char getTaxiLocation()
    {
        return taxiLocation;
    }

    public void setTaxiLocation(char taxiLocation)
    {
        this.taxiLocation = taxiLocation;
    }

    public int getTotalAmt()
    {
        return totalAmt;
    }

    public void setTotalAmt(int totalAmt)
    {
        this.totalAmt = totalAmt;
    }

    public int getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(int availableFrom) {
        this.availableFrom = availableFrom;
    }


    Taxi( String taxiName,char taxiLocation, int totalAmt,int availableFrom)
    {
        this.taxiName = taxiName;
        this.taxiLocation = taxiLocation;
        this.totalAmt = totalAmt;
        this.availableFrom=availableFrom;
    }

}



public class Main
{
    public static void main(String args[])
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter How many Taxi's you want : ");
        int noOfTaxi = sc.nextInt();
        HashMap<String, Taxi> taxis = Booking.autoFillTaxiDetails(noOfTaxi);

//        System.out.println("Get taxi information");
//        int taxino = sc.nextInt();
//        Taxi taxi1 = Booking.getTaxiInfo(taxis, taxino);
//        System.out.println(taxi1.getTaxiID()+" "+ taxi1.getTaxiName()+" "+taxi1.getTotalAmt()+" "+taxi1.getTaxiLocation()+" "+taxi1.getAvailableFrom());

        System.out.println("Call Taxi Booking : ");
        int i =1;
        int bookkID;
        int customerID;
        char pickupPoint;
        char dropPoint;
        int pickupTime;
        while(true)
        {
            System.out.println("1. Show All taxi details");
            System.out.println("2. Show single taxi");
            System.out.println("3. Start booking");
            System.out.println("4. Exit");
            System.out.println("Enter the input : ");
            int k = sc.nextInt();

            switch (k)
            {
                case 1 :
                    Booking.getAllTaxisList();
                    break;

                case 3 :
                    System.out.println("Input "+i);
                    System.out.println("CustomerID : ");
                    customerID = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Pickup Point : ");
                    pickupPoint = sc.nextLine().charAt(0);
                    System.out.println("Drop Point : ");
                    dropPoint = sc.nextLine().charAt(0);
                    System.out.println("Pickup Time : ");
                    pickupTime = sc.nextInt();

                    Booking book =new Booking(customerID,pickupPoint,dropPoint,pickupTime);

                    book.allocateTaxiForBooking(pickupPoint,dropPoint, pickupTime);
                    System.out.println("Want to continue booking :");
                    int l = sc.nextInt();
                    if(l == 0)
                    {
                        break;
                    }
                    i++;
                case 4 :
                    return;
            }
        }
   }
}


class Booking
{
    UUID bookkID;
    int customerID;
    public static char pickupPoint;
    public static char dropPoint;
    static int pickupTime;
    static HashMap<String, Taxi> taxis = new HashMap<>();
    static ArrayList<Point> points = new ArrayList();

    Booking(int customerID,char pickupPoint,char dropPoint,int pickupTime)
    {
        this.bookkID = UUID.randomUUID();
        this.customerID = customerID;
        this.pickupPoint = pickupPoint;
        this.dropPoint = dropPoint;
        this.pickupTime = pickupTime;
        autoFillPointDetails();

    }

    public static HashMap<String, Taxi> autoFillTaxiDetails(int n)
    {
        for(int i =1;i<=n;i++)
        {
            Taxi a = new Taxi( "Taxi-"+i, 'A', 0, 0);
            taxis.put(a.getTaxiName(),a);
        }
        return taxis;
    }

    public static void getAllTaxisList()
    {
        for(Map.Entry<String, Taxi> taxik : taxis.entrySet())
        {
            Taxi taxi1 = taxik.getValue();
            System.out.println(taxi1.getTaxiName()+" "+taxi1.getTotalAmt()+" "+taxi1.getTaxiLocation()+" "+taxi1.getAvailableFrom());
        }
    }

    public static Taxi getTaxiInfo(ArrayList<Taxi> taxis, int n)
    {
        return taxis.get(n-1);
    }

    public static ArrayList<Point> autoFillPointDetails()
    {
        points.add(new Point('A', 0));
        points.add(new Point('B', 15));
        points.add(new Point('C', 30));
        points.add(new Point('D', 45));
        points.add(new Point('E', 60));
        points.add(new Point('F', 75));
        return points;
    }

    public void allocateTaxiForBooking(char pickup, char dropPoint, int time)
    {
        Taxi availableTaxiclass = null;
        availableTaxiclass = findAvailableTaxi(pickup);
        if(availableTaxiclass != null)
        {
            availableTaxiclass.setTaxiLocation(dropPoint);
            calculateTaxiAmt(availableTaxiclass, pickupPoint, dropPoint);
            String taxID = availableTaxiclass.getTaxiName();
            availableTaxiclass.setAvailableFrom(time);
            Taxi k = taxis.get(taxID);
            taxis.put(taxID, availableTaxiclass);
        }

        if(availableTaxiclass != null)
        {
            System.out.println("Taxi can be booked");
            System.out.println(availableTaxiclass.getTaxiName()+" is booked");
        }
    }

    public static Taxi findAvailableTaxi(char pickupPoint)
    {
        Taxi taxi1 = null;
        ArrayList<Taxi> availableTaxi = new ArrayList<>();
        for(Map.Entry<String , Taxi> taxik : taxis.entrySet())
        {
            Taxi taxi = taxik.getValue();
            char taxiLocation = taxi.getTaxiLocation();
            if(taxiLocation == pickupPoint && pickupTime >= taxi.getAvailableFrom())
            {
                availableTaxi.add(taxi);
            }
        }
        if (!availableTaxi.isEmpty())
        {
            availableTaxi.sort(Comparator.comparingInt(Taxi::getTotalAmt));
            return availableTaxi.get(0);
        }
        else
        {
            // Find the nearest point with available taxis
            taxi1 = findNearestPointWithTaxis(pickupPoint);
            if(taxi1 == null)
            {
                System.out.println("Currently No taxis can be allocated");
                return null;
            }
        }
        return taxi1;
    }

    public static Taxi findNearestPointWithTaxis(char pickupPoint)
    {
        Taxi taxi1 = null;
        int amt = 0;
        int pickupIndex = -1;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getPoint() == pickupPoint) {
                pickupIndex = i;
                break;
            }
        }
        if (pickupIndex != -1) {
            // Search for the nearest point with available taxis
            for (int i = 1; i < points.size(); i++) {
                int prevPointIndex = (pickupIndex - i + points.size()) % points.size();
                int nextPointIndex = (pickupIndex + i) % points.size();
                char prevPoint = points.get(prevPointIndex).getPoint();
                char nextPoint = points.get(nextPointIndex).getPoint();
                for(Map.Entry<String , Taxi> taxik : taxis.entrySet())
                {
                    Taxi taxi = taxik.getValue();
                    if (taxi.getTaxiLocation() == prevPoint || taxi.getTaxiLocation() == nextPoint)
                    {
                       taxi1 = getTaxiWithLeastAmt(taxi, amt);
                    }
                }
            }
        }
        return taxi1; // No nearby point with available taxis
    }

    public static Taxi getTaxiWithLeastAmt(Taxi taxi, int amt)
    {
        if(amt > taxi.getTotalAmt())
        {
            int k = taxi.getTotalAmt();
            amt = k;
            return taxi;
        }
        return null;
    }

    public void calculateTaxiAmt(Taxi taxi,char pickupPoint,char DropPoint)
    {
        int amt = taxi.getTotalAmt();
        int l = (DropPoint - 0) - (pickupPoint - 0);
        l = 15 * l;
        l -= 5;
        if(l> 0)
        {
            int k = l*10;
            l = k+100;
            amt += l;
            taxi.setTotalAmt(amt);
        }
    }
}




