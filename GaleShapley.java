import java.util.*;

public class GaleShapley {

    public static Map<Integer, String> galeShapley(int[] programmers, String[] companyNames, int[][] companyPreferences) {
        Map<Integer, List<Integer>> programmerPreferences = new HashMap<>();
        Map<String, List<Integer>> companyPreferencesMap = new HashMap<>();

        // Stores the Preferences of the Programmers
        for (int i = 0; i < programmers.length; i++) {
            programmerPreferences.put(programmers[i], new ArrayList<>());
            for (int j = 1; j < companyPreferences[i].length; j++) {
                programmerPreferences.get(programmers[i]).add(companyPreferences[i][j]);
            }
        }
        // Stores the Preferences of the Companies
        for (int i = 0; i < companyNames.length; i++) {
            String companyName = companyNames[i];
            companyPreferencesMap.put(companyName, new ArrayList<>());
            for (int j = 1; j < companyPreferences[i].length; j++) {
                companyPreferencesMap.get(companyName).add(companyPreferences[i][j]); 
            }
        }
        // Creating Objects to store the Matchings
        Map<Integer, String> programmerEngagements = new HashMap<>();
        Map<String, Integer> companyEngagements = new HashMap<>();
        Queue<Integer> unengagedProgrammers = new LinkedList<>();

        for (int programmer : programmers) {
            unengagedProgrammers.add(programmer);
        }

        while (!unengagedProgrammers.isEmpty()) {
            int programmer = unengagedProgrammers.poll();
            List<Integer> companyPreferencesForProgrammer = programmerPreferences.get(programmer);

            for (int companyIndex : companyPreferencesForProgrammer) {
                String companyName = companyNames[companyIndex - 1]; // Convert first index to company name
                Integer currentPartnerIndex = companyEngagements.get(companyName);

                if (currentPartnerIndex == null) {
                    // Checks if a company has a match if not match them.
                    programmerEngagements.put(programmer, companyName);
                    companyEngagements.put(companyName, programmer);
                    break;
                } else {
                    // in the case that the company has a match then check if it prefers this programmer
                    List<Integer> companyPreferencesForCurrentPartner = companyPreferencesMap.get(companyName);
                    int currentPartner = programmers[currentPartnerIndex];
                    int programmerIndex = companyPreferencesForCurrentPartner.indexOf(programmer);

                    if (programmerIndex < companyPreferencesForCurrentPartner.indexOf(currentPartner)) {
                        // The Company perfers this Programmer over the other so now it will swap with it.
                        programmerEngagements.put(programmer, companyName);
                        companyEngagements.put(companyName, programmer);
                        unengagedProgrammers.add(currentPartner);
                        break;
                    }
                }
            }
        }

        return programmerEngagements;
    }

    //Checks if the pairs are satisfactory
    public static boolean arePairsSatisfactory(Map<Integer, String> programmerEngagements, String[] companyNames, int[][] companyPreferences) {
        Map<String, Integer> companyIndices = new HashMap<>();

        // Creates a Connection between company names and their index values(their preferences)
        for (int i = 0; i < companyNames.length; i++) {
            companyIndices.put(companyNames[i], i);
        }

        // we go over and grab every pair of programmer and company
        for (Map.Entry<Integer, String> entry : programmerEngagements.entrySet()) {
            int programmer = entry.getKey();
            String company = entry.getValue();

            List<Integer> companyPreferencesForProgrammer = new ArrayList<>();
            List<Integer> companyPreferencesForCurrentPartner = new ArrayList<>();

            // Get the preferences of the programmer and the current Company
            for (int i = 1; i < companyPreferences[programmer - 1].length; i++) {
                companyPreferencesForProgrammer.add(companyPreferences[programmer - 1][i]);
            }

            if (programmerEngagements.containsValue(company)) {
                int currentPartner = getKeyByValue(programmerEngagements, company);
                for (int i = 1; i < companyPreferences[currentPartner - 1].length; i++) {
                    companyPreferencesForCurrentPartner.add(companyPreferences[currentPartner - 1][i]);
                }
            }

            //Now we go through every Programmer to check the companies that it prefers over its
            // current match would prefer the programmer over themselves and would return false if so
            int programmerIndex = companyPreferencesForProgrammer.indexOf(companyIndices.get(company));
            for (int i = 0; i < programmerIndex; i++) {
                String preferredCompany = companyNames[companyPreferencesForProgrammer.get(i) - 1];
                int preferredCompanyIndex = companyIndices.get(preferredCompany);
                if (companyPreferencesForCurrentPartner.indexOf(preferredCompanyIndex) < companyPreferencesForCurrentPartner.indexOf(companyIndices.get(company))) {
                    return false;
                }
            }
        }

        return true;
    }

    public static int getKeyByValue(Map<Integer, String> map, String value) {
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return -1; // Not found
    }



    public static void main(String[] args) {
        int[] programmers = {1, 2, 3, 4, 5};
        String[] companyNames = {"Amazon", "BestBuy", "C", "D", "E"};
        int[][] companyPreferences = {
            {1, 2, 1, 5, 1, 2},
            {2, 5, 2, 3, 3, 3},
            {3, 1, 3, 2, 2, 5},
            {4, 3, 4, 1, 4, 4},
            {5, 4, 5, 4, 5, 1}
        };

        Map<Integer, String> satisfactoryPairing = galeShapley(programmers, companyNames, companyPreferences);

  
        for (Map.Entry<Integer, String> entry : satisfactoryPairing.entrySet()) {
            System.out.println("Programmer " + entry.getKey() + " is paired with Company " + entry.getValue());
        }
        System.out.println(arePairsSatisfactory(satisfactoryPairing, companyNames, companyPreferences));

    }
}
// How to test: 


