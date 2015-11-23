package org.enthusia.app.enthusia.model;

import org.enthusia.app.R;

public final class EnthusiaEvents {

    public final static int[] drawables = {
            R.drawable.img_enthu_cricket,
            R.drawable.img_enthu_football,
            R.drawable.img_enthu_basketball,
            R.drawable.img_enthu_volleyball,
            R.drawable.img_enthu_rink,
            R.drawable.img_enthu_tennis,
            R.drawable.img_enthu_tt,
            R.drawable.img_enthu_kabaddi,
            R.drawable.img_enthu_athletics,
            R.drawable.img_enthu_throwball,
            //R.drawable.img_enthusia_squash,
            //R.drawable.img_enthusia_boxing,
            R.drawable.img_enthu_carrom,
            R.drawable.img_enthu_chess,
            //R.drawable.img_enthusia_dodgeball,
            R.drawable.img_enthu_badminton,
            R.drawable.img_enthu_khokho,
            R.drawable.img_enthu_swimming,
            R.drawable.img_enthu_marathon,
            //R.drawable.img_enthusia_handball,
            R.drawable.img_enthu_cyclothon
    };

    public final static String[] events = {
            "Cricket",
            "Football",
            "Basketball",
            "Volleyball",
            "Rink Football",
            "Tennis",
            "Table Tennis",
            "Kabaddi",
            "Athletics",
            "Throwball",
            //"Squash",
            //"Boxing",
            "Carrom",
            "Chess",
            //"Dodgeball",
            "Badminton",
            "Kho Kho",
            "Swimming",
            "Marathon",
            //"Handball",
            "Cyclothon"
    };

    public final static int[] rules = {
            R.string.enthusia_cricket,
            R.string.enthusia_football,
            R.string.enthusia_basketball,
            R.string.enthusia_volleyball,
            R.string.enthusia_rink,
            R.string.enthusia_tennis,
            R.string.enthusia_tt,
            R.string.enthusia_kabaddi,
            R.string.enthusia_athletics,
            R.string.enthusia_throwball,
            R.string.enthusia_carrom,
            R.string.enthusia_chess,
            R.string.enthusia_badminton,
            R.string.enthusia_kho,
            R.string.enthusia_swimming,
            R.string.enthusia_handball,
            R.string.enthusia_handball
    };

    public static String[] getEventHead (int position) {
        switch (position) {
            case 0: // Cricket done
                return new String[] { "Shubham Shah: +918625840845", "Rohit Laddhad: +919730303015", "Chinmay Navrange: +919619190906", "Shreyas Kolhe: +919623061058"};
            case 1: // Football done
                return new String[] { "Aditya Shah: +919594196106", "Gaurav Shetty: +919769633153", "Harsh Sanjay: +919619699237", "Kaushal Suvarna: +918082888028", "Kevin Daftary: +919022641507" };
            case 2: // Basketball done
                return new String[] { "Rhishabh Deshpande: +919833839981", "Tirth Doshi: +919869690406", "Sayyed Fahim: +919920096802", "Abhishek Singh: +919004118181", "Ayesha Passanha: +919167130184","Nikhita Mirchandani: +919004409222" };
            case 3: // Volleyball done
                return new String[] { "Shubham kamble: +918898917665", "omprakash kale: +919930596003", "Ravi Majkuri: +918600275048", "Madhuri Patil: +917718041260", "Pratiksha Dehade: +919699886355" };
            case 4: // Rink Football
                return new String[] { "Kevin Jain: +918108478609","Aditya Jain: +919167206686","Rushit Shah: +919819725464","Zubiya Reshmwala: +919969949110","Kalyani Kamble: +918080449469" };
            case 5: // Tennis done
                return new String[] { "Joel D'souza: +919920241397", "Raj chandak: +919664858392", "Neha Gosula: +919920227755"};
            case 6: // Table Tennis done
                return new String[] { "Ayush Lokhande: +918390133669", "Hemant Wadekar: +918108040003", "Archit Joshi: +917208907328", "komal deoda: +919699229666", };
            case 7: // Kabaddi done
                return new String[] { "Pravin Mane: +919890186399", "Mahesh Pentewad: +918976307617", };
            case 8: // Athletics done
                return new String[] { "Hem Kothari: +918286037294", "Dharmit Prajapati: +919920445979", "Amit Kharwandikar: +919869056872", "Heramb Mathkar: +919167147950", "Swaroop Atnoorkar: +918108282968","mandavi tripathi: +918080848183" };
            case 9: // Throwball done
                return new String[] { "Poorvi Nagaria: +919819150640", "Sanika Raut: +919920250229" };

            /*
            case 10: // Squash
                return new String[] { "Nikhil Shetty: +918097875058", "Akshay Pawar: +917588516680" };
            case 11: // Boxing
                return new String[] { "Nikhil Shetty: +918097875058", "Akshay Pawar: +917588516680" };
            */

            case 10: // Carrom done
                return new String[] { "Akshay Ghodke: +918237526496", "Siddhant Jajoo: +919594388339" };
            case 11: // Chess done
                return new String[] { "Sagar Hinduja: +919619287347", "Roshan Dukale: +919604088406", "Ritesh Surve: +919769772422","Richa Nagda: +918828866168" };

            /*
            case 14: // Dodgeball
                return new String[] { "Sheetal Mhaske: +919665090044", "Swati Dighole: +919768350702" };
            */

            case 12: // Badminton done
                return new String[] { "Sanjay Laungani: +919920276784", "Utsav Gupta: +919967037400", "Nirmal Patil: +919765396987", "Ketaki Kothawade: +919870851092", "Krushangi Chandekar: +919869163390" };
            case 13: // Kho Kho done
                return new String[] { "Sachin tayade: +917303276006", "Swaraj shevgan: +919404872671", "Sheetal patole: +917498712317", "Neeta Arote: +918655358122" };
            case 14: // Swimming done
                return new String[] { "Arpit Sharma: +919833442805", "Sohan Ghanchi: +919867373370" };
            case 15: // Marathon done
                return new String[] { "Rushabh Kapadia: +919833863639", "Harsh Mehta: +919167266569", "Siddhesh Nair: +919920581474", "Neha Gosula: +919920227755" };
            /*
            case 19: // Handball
                return new String[] { "Rishikesh: +919987722085" };
            */

            case 16: // Cyclothon done
                return new String[] { "Rushabh Kapadia: +919833863639", "Harsh Mehta: +919167266569", "Siddhesh Nair: +919920581474", "Neha Gosula: +919920227755" };
        }
        return new String[] { "Nikhil Shetty: +918097875058", "Akshay Pawar: +917588516680" };
    }

}
