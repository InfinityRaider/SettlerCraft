package com.InfinityRaider.settlercraft.settlement.settler;

import java.util.Random;

public class SettlerRandomizer {
    private static final SettlerRandomizer INSTANCE = new SettlerRandomizer();

    private final Random random;

    public static SettlerRandomizer getInstance() {
        return INSTANCE;
    }

    private SettlerRandomizer() {
        this.random = new Random();
    }

    public boolean getRandomGender() {
        return random.nextBoolean();
    }

    public String getRandomFirstName(boolean male) {
        if(male) {
            return FIRST_NAMES_MALE[random.nextInt(FIRST_NAMES_MALE.length)];
        } else {
            return FIRST_NAMES_FEMALE[random.nextInt(FIRST_NAMES_FEMALE.length)];
        }
    }

    public String getRandomSurname() {
        return SURNAMES[random.nextInt(SURNAMES.length)];
    }

    public final String[] SURNAMES = new String[] {
            "Abrams",
            "Ackeret",
            "Ackerman",
            "Adelbern",
            "Aldryn",
            "Archer",
            "Arryn",
            "Baggins",
            "Bing",
            "Black",
            "Brahms",
            "Calhoun",
            "Carpenter",
            "Cartman",
            "Dagger",
            "Eastwood",
            "Franklin",
            "Freeman",
            "Flanders",
            "Geller",
            "Griffin",
            "Jackson",
            "Jones",
            "Johnson",
            "Kane",
            "Kleiner",
            "Knight",
            "Krieger",
            "Lamar",
            "Lee",
            "Loggins",
            "Marsh",
            "Mattox",
            "Morgan",
            "Mosby",
            "Prandtl",
            "Redwood",
            "Reynolds",
            "Rigby",
            "Rosenberg",
            "Stark",
            "Stinson",
            "Stotch",
            "Valentine",
            "Valmer",
            "Vance",
            "White",
            "Watson",
            "Wolfson",
            "Woodhouse",
    };

    public final String[] FIRST_NAMES_MALE = new String[] {
            "Adam",
            "Albert",
            "Alphonse",
            "Balthazar",
            "Barney",
            "Bart",
            "Benjamin",
            "Billy",
            "Brian",
            "Bruce",
            "Bobby",
            "Carl",
            "Chandler",
            "Clint",
            "Christopher",
            "David",
            "Dexter",
            "Duncan",
            "Edward",
            "Eli",
            "Eren",
            "Eric",
            "Erwin",
            "Finn",
            "Gordon",
            "Harry",
            "Ian",
            "Isaac",
            "Jack",
            "James",
            "Jason",
            "Jeremy",
            "Jet",
            "John",
            "Josh",
            "Kenny",
            "Kevin",
            "Kyle",
            "Lewis",
            "Ladd",
            "Levi",
            "Lou",
            "Marshall",
            "Michael",
            "Morgan",
            "Mortimer",
            "Morty",
            "Ned",
            "Patrick",
            "Paul",
            "Peter",
            "Randy",
            "Richard",
            "Rick",
            "Ronald",
            "Rupert",
            "Ryder",
            "Spike",
            "Stan",
            "Steve",
            "Stirling",
            "Thomas",
            "Tony",
            "Trevor",
            "Troy"
    };

    public final String[] FIRST_NAMES_FEMALE = new String[] {
            "Alice",
            "Alyx",
            "Angela",
            "Angie",
            "Annie",
            "Bulma",
            "Casey",
            "Carmen",
            "Chelsea",
            "Cheryl",
            "Debra",
            "Eleanor",
            "Elizabeth",
            "Ennis",
            "Faye",
            "Felicia",
            "Grace",
            "Hannah",
            "Heidi",
            "Heloise",
            "Henrietta",
            "Jane",
            "Jeanny",
            "Jennifer",
            "Joan",
            "Jody",
            "Jolene",
            "Jude",
            "Judith",
            "Katherine",
            "Kim",
            "Krista",
            "Lana",
            "Leia",
            "Lily",
            "Lisa",
            "Lucy",
            "Malory",
            "Maria",
            "Marina",
            "Mary",
            "Megan",
            "Miria",
            "Misty",
            "Monica",
            "Myrtle",
            "Pam",
            "Patricia",
            "Pepper",
            "Phoebe",
            "Polly",
            "Rachel",
            "Rey",
            "Robyn",
            "Roxanne",
            "Sally",
            "Sasha",
            "Sherane",
            "Suzanne",
            "Suzy",
            "Wendy"
    };
}
