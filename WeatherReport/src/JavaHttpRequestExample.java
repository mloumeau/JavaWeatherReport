import org.json.*;
import java.text.*;
import java.util.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class JavaHttpRequestExample {
    public static void main(String args[]){
          initialize();
    }
    
    public static void initialize() {
    	//This is here so it won't need to be in getWeatherReport.
    	//Since it is here, it prevents the program from asking if the user wants to save multiple times.
    	String tryAgain="no";
    	boolean saving = false;
    	PrintStream stdout = System.out;
    	PrintStream output = System.out;
    	if (saveToFile().equals("yes")) {
    		saving = true;
    		output = createAndWriteToFile();
    	}
    	getWeatherReport(tryAgain, saving, stdout, output);
    }

    
    public static String saveToFile() {
    	
    	//Despite the warnings, don't close scanner. It will close all standard outputs.
        Scanner myObj = new Scanner(System.in);
    	String save;
    	
        System.out.println("Would you like to save upcoming results to a file?(yes/no)"); 
        save = myObj.nextLine().toLowerCase();
        
        return save;
    }
    

    public static PrintStream writeToFile(String fileName) {
    	PrintStream output = System.out;
        try {
        	//This tells the program where to print its output.
        	//In this case, it will be the name of the file the user created.
        	//The true means it append=true, meaning it will append, not overwrite.
            output = new PrintStream(new FileOutputStream(fileName, true));
        }
        catch(Exception e) {
            e.getStackTrace();
        }
        return output;
      }
    
    
    public static PrintStream createAndWriteToFile() {
    	
    	//Despite the warnings, don't close scanner. It will close all standard outputs.
        Scanner name = new Scanner(System.in);
        String fileName;
        PrintStream output = System.out;
	      try {
    
	        System.out.println("Name of file to use/create? (filename.txt)"); 
	        fileName = name.nextLine();
	        		
	        //Check to see if the file exists
	        //If it doesn't, create the file and return the name of the file for the PrintStream and FileOutputStream
	        File myObj = new File(fileName);
	        	if (myObj.createNewFile()) {
	        		System.out.println("File created: " + myObj.getName());
	        		return writeToFile(fileName);
	        } 
	        	//If it does, the contents will be appended to the file.
	        	else {
	        		System.out.println("File exists, results will be appended.");
	        		return writeToFile(fileName);
	        }
	      } catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }
      	return output;
    }
    
    
    public static String getCity(String tryAgain, boolean saving, PrintStream stdout, PrintStream output) {
    	
    	//Despite the warnings, don't close scanner. It will close all standard outputs.
        Scanner myObj = new Scanner(System.in);
    	String city;
    	//A unique API key that will work for my machine.
    	String APPID = "b702a4286daab5d7af2a7409d0a68275";
    	
    	//Different print statements on whether or not they choose to save.
    	if (saving) { 
    		System.out.println("\nEnter City: (Results will be stored in file)"); 
    	}
    	
    	else {
    		System.out.println("\nEnter City: ");
    	}
    	//First make it all lowercase.
        city = myObj.nextLine().toLowerCase();
      
			 try {
			  		URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+APPID);
		            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
		            
		            //If the city doesn't exist, this line will trigger an exception.
		            InputStreamReader inputStreamReader=new InputStreamReader(httpURLConnection.getInputStream());
		
				}
			 
			catch (Exception e) {
				
					System.out.println("\nThe city you requested, "+ city +", does not exist.");
					
					//The false parameter tells the function to print the statement about having a wrong city input.
					tryAgain = tryAgain(false);
					if (tryAgain.equals("yes")){
							//All the parameters were passed in so we could call getWeatherReport.
							getWeatherReport(tryAgain, saving, stdout, output);
				}
					else 
						System.out.println("\nThanks!");
						System.exit(0);
			}
		//Return the lower case string, with the first letters of each word capitalized.
        return capitalize(city);
    }
    
    
    public static String capitalize(String message) {
    	
    	//Find the spaces, capitalize what's after.
        char[] charArray = message.toCharArray();
        boolean foundSpace = true;
        for(int i = 0; i < charArray.length; i++) {
          if(Character.isLetter(charArray[i])) {
            if(foundSpace) {
              charArray[i] = Character.toUpperCase(charArray[i]);
              foundSpace = false;
            }
          }
          else 
            foundSpace = true;
        }
        //String.valueOf converts to a string.
       return String.valueOf(charArray);
      }
    
 
    
    public static String tryAgain(boolean trueCity) {
    	
    	//Despite the warnings, don't close scanner. It will close all standard outputs.
        Scanner myObj = new Scanner(System.in);
    	String tryAgain;
    	
	    	if (trueCity) {
	    		System.out.println("\nWould you like to see the weather for another city as well?(yes/no)");
	    	}
	    	else {
	    		System.out.println("Would you like to try again?(yes/no)");
	    	}
        tryAgain = myObj.nextLine().toLowerCase();
        
        return tryAgain;
    }  

    
    public static String KtoF(double temp) {
    	
    	//Convert to string to be able to concat with print statements
        return String.format("%.2f",(temp - 273.15) * 9/5 + 32);
    }
    

    public static String getDateTime(long time, String f) throws InterruptedException {
    	
    		//Takes in milliseconds
            Date date = new Date(time);
            //The "f" parameter is to specify what we want from the time stamp.
            DateFormat format = new SimpleDateFormat(f);
            format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
            return format.format(date);
        }
    
    
    
    public static void getWeatherReport(String tryAgain, boolean saving, PrintStream stdout, PrintStream output){

    	do { //do-while loop every time the user wants to input another city.

        try {

        	//Pass in all the parameters since getCity calls getWeatherReport
        	String city = getCity(tryAgain, saving, stdout, output);
  	  		URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=b702a4286daab5d7af2a7409d0a68275");

  	  		//Create connections to the API
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            //Get the response JSON
            String line="";
            InputStreamReader inputStreamReader=new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            StringBuilder response=new StringBuilder();
            while ((line=bufferedReader.readLine())!=null){
                response.append(line);
            }
            bufferedReader.close();
            
            JSONObject jsonObject=new JSONObject(response.toString());
            
            //Set the out to the file, if they saved
        	System.setOut(output);

        		//Parse through the JSON like Python
            	//JSONObject = {}, JSONArray = []
            	System.out.println("===========================================================");
                System.out.println("Location    : "+jsonObject.getString("name") + ", " + jsonObject.getJSONObject("sys").getString("country"));
            	System.out.println("Date/Time   : "+getDateTime(System.currentTimeMillis() + (jsonObject.getInt("timezone")*1000), "MM/dd/yyyy HH:mm:ss"));
                System.out.println("Description : "+ capitalize(jsonObject.getJSONArray("weather").getJSONObject(0).getString("description")));
                System.out.println("Temperature : "+KtoF(jsonObject.getJSONObject("main").getDouble("temp"))+"°F");
                System.out.println("  -High     : "+KtoF(jsonObject.getJSONObject("main").getDouble("temp_max"))+"°F");
                System.out.println("  -Low      : "+KtoF(jsonObject.getJSONObject("main").getDouble("temp_min"))+"°F");
                System.out.println("Sunrise     : "+getDateTime((jsonObject.getJSONObject("sys").getLong("sunrise")+jsonObject.getLong("timezone"))*1000, "HH:mm:ss"));
                System.out.println("Sunset      : "+getDateTime((jsonObject.getJSONObject("sys").getLong("sunset")+jsonObject.getLong("timezone"))*1000, "HH:mm:ss"));
                System.out.println("===========================================================");
                
            //Set it back to show the rest    
            System.setOut(stdout);
        }
        catch (Exception e){
            System.out.println("Error in Making Get Request");
        }
        
        //Boolean in parameter gives out different print statements weather or not the city they input was a real city.
        tryAgain=tryAgain(true);
        
      }while(tryAgain.equals("yes"));
    	
    		System.out.println("\nThanks!");
        }
    
    }
    