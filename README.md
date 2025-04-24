# advertisements-app

A user-friendly web application designed to manage various types of ads. Users can view, create, and interact with ads for different categories like Cars, Real Estate, and Pets. The app provides an easy way to view and create ads with detailed information.  

## **Installation**
1. Navigate to the directory where you want to clone the project.  
In your terminal, type:  
```sh
cd path/to/your/directory
```
2. Clone the project using:  
```sh
git clone https://github.com/Luisa-Elena/advertisements-app.git
```
3. For backend:  
3.1. Navigate to the cloned folder for backend:
```sh
cd advertisements-app/backend
```
4.For frontend:  
4.1. Navigate to the cloned folder for frontend:
```sh
cd advertisements-app/frontend/fe-app
```
4.2. Now type:
```sh
npm install
```
4.3. For starting the frontend app:
```sh
npm start
```


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
![Image](https://github.com/user-attachments/assets/57b5cc73-f60d-4414-b291-5054610a5e93)

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


### Code Flow for posting a new ad:

1. **Controller**:
   - Gets from the request body the json with data for the new ad.
   - Converts the json to a Map<String, Object>
   - Calls the `save` method from the service layer with the map.  

2. **Service**:
   - The `save` method receives a map of field names and values, which represent the ad attributes.
   - It retrieves the **ad type** from the map and uses the **`Registry`** to look up the appropriate **ad class**.
   - The **`Registry`** holds a Map bhaving as key the ad type and the value the specific ad class for that type. Example: "CAR" -> CarAd.class  
   - The `AdBuilder` then creates an instance of the ad class using the constructor matching the provided field values.

3. **`AdBuilder.buildAd(Class<? extends Ad> adClass, Map<String, Object> fieldValueMap)`**:
   - The `buildAd` method uses reflection to find the suitable constructor for the ad class and match the required parameters with the values in the field-value map.
   - The method ensures that the field values are of the correct type (e.g., `String`, `int`, `double`) and creates the ad instance dynamically.
   - If all required parameters are provided, a new ad instance is created and returned.

4. **Repository**:
   - Interacts with the database via SQL queries to get ads or save a new ad.  
  

### Steps for adding a new ad type to the app:
1. Extend the base class Ad and create a new model class for the new advertisement, containing all desired fields to store information for the new ad type.
2. Put in the registry map the new ad type and its corresponding class created previously.
3. Put the new ad type in the databse to be fetched by the frontend when a user has to choose the type of ad he wants to insert.
4. Create a form for this new ad on the frontend.
5. The fields in JSON received from the frontend must match the name of the fields in the concrete ad class for the AdBuilder to construct a new instance of that ad.



## Database
![Image](https://github.com/user-attachments/assets/35423bbd-6bca-45bd-9895-dbadb6e935cd)

Idea: Store specific ad fields in a json, instead of creating separate tables for each ad type. This approach makes it easier to get all ads.  
