# advertisements-app

A user-friendly web application designed to manage various types of ads. Users can view, create, and interact with ads for different categories like Cars, Real Estate, and Pets. The app provides an easy way to search, create, and view ads with detailed information.  

## Features
- **View All Ads**: Fetch and display all ads in a card format with their details.
- **Post New Ads**: Users can post new ads by selecting a category (Car, Real Estate, Pet) and filling out a form for that specific category.
- **View Single Ad**: Detailed view of each ad when clicked.

## Example usage  
First, the user can select what operation to perform: get all ads or post a new ad:  
![Image](https://github.com/user-attachments/assets/7152f421-8171-4bcb-9a32-9292d87e1e02)  

Get all ads example:  
![Image](https://github.com/user-attachments/assets/ea098d03-3979-43a9-87b7-9a7849b62cb7)  

Now if the user clicks on a specific ad, he can view all the details for that ad:  
![Image](https://github.com/user-attachments/assets/20fd1da5-009a-4831-ae94-53fc133e3bd1)

Post a new ad example:
First, the user must choose what type of ad he wants to post, in order to get the specific form for that ad type:  
![Image](https://github.com/user-attachments/assets/63482048-6b7f-4cf7-9566-047e95ea314d)  

Then he has to complete the form adn submit the data. If everything is ok a success message is displayed:  
![Image](https://github.com/user-attachments/assets/ea64ea7f-f62e-42e8-8bcb-2e249ba04dc9)  



## API Endpoints
- `GET /api/ads`: Fetch all ads.
- `GET /api/ads/{id}`: Fetch a single ad by its ID.
- `GET /api/types`: Fetch available ad types (e.g., Car, Real Estate, Pet).


## Example Data
Here's an example of how to post a **Car** ad via a `curl` request:
```bash
curl -X POST http://localhost:8080/api/ads \
-H "Content-Type: application/json" \
-d '{
  "type": "CAR",
  "description": "some description",
  "price": 20000,
  "location": "Cluj-Napoca",
  "brand": "Toyota"
}'
```


## Backend Reflection for Ad Creation

In the backend, we use **reflection** to dynamically create instances of different ad types (e.g., Car Ads, Real Estate Ads, Pet Ads). This approach allows for flexible and scalable ad management.

### How It Works

1. **Ad Type Mapping**:
   Each advertisement type is represented by a specific class (e.g., `CarAd`, `RealEstateAd`, `PetAd`). These are subclasses of the Ad class which has the common fields for all advertisements - description, location and price. When a new ad is created, the **ad type** is passed along with the other field values.  

2. **Reflection and Registry**:
   - When the backend receives the request to create a new ad, it checks the **ad type** (`"CAR"`, `"REAL_ESTATE"`, `"PET"`, etc.) and uses it to look up the corresponding **ad class** in the **`Registry`**.
   - The `Registry` maintains a mapping between the ad types (as strings) and their corresponding ad classes (for example: "PET" maps to PetAd.class).

3. **Ad Creation with Reflection**:
   - The backend uses the **`AdBuilder`** class to dynamically create an instance of the corresponding ad type.
   - The **`AdBuilder`** uses reflection to inspect the constructor of the specific ad class and matches the constructor parameters with the values provided in the **field-value map**. The `AdBuilder` then uses this data to instantiate the ad class and populate its fields.
   - The field-value map is just a Map<String, Object> of the JSON with data received from the frontend when a form for posting a new ad was submitted.  

4. **Saving the Ad**:
   - Once the ad object is created, it's saved into the **ad repository** using the `adRepository.save()` method.

### Code Flow for posting a new ad:

1. **Controller**:
   - Gets from the request body the json with data for the new ad.
   - Converts the json to a Map<String, Object>
   - Calls the `save` method from the service layer with the map.  

2. **Service**:
   - The `save` method receives a map of field names and values, which represent the ad attributes.
   - It retrieves the **ad type** from the map and uses the **`Registry`** to look up the appropriate **ad class**.
   - The `AdBuilder` then creates an instance of the ad class using the constructor matching the provided field values.

3. **`AdBuilder.buildAd(Class<? extends Ad> adClass, Map<String, Object> fieldValueMap)`**:
   - The `buildAd` method uses reflection to find the suitable constructor for the ad class and match the required parameters with the values in the field-value map.
   - The method ensures that the field values are of the correct type (e.g., `String`, `int`, `double`) and creates the ad instance dynamically.
   - If all required parameters are provided, a new ad instance is created and returned.

4. **Repository**:
   - Stores all ads in a `ConcurrentHashMap<Integer, Ad>` where the Integer value is the ID of the ad.
