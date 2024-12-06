package com.paccy.service.serviceimplimentation;

import com.paccy.dto.RestaurantDto;
import com.paccy.model.Address;
import com.paccy.model.ContactInformation;
import com.paccy.model.Restaurant;
import com.paccy.model.User;
import com.paccy.repository.AddressRepository;
import com.paccy.repository.RestaurantRepository;
import com.paccy.repository.UserRepository;
import com.paccy.request.CreatRestaurantRequest;
import com.paccy.service.RestaurantService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService  {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;
    @Override
    public Restaurant createRestaurant(CreatRestaurantRequest req, User user)
    {
        Address address=addressRepository.save(req.getAddress());
        Restaurant restaurant=new Restaurant();
        restaurant.setAddress(address);
        restaurant.setContactInformation(req.getContactInformation());
        restaurant.setCuisineType(req.getCuisineType());
        restaurant.setDescription(req.getDescription());
        restaurant.setImages(req.getImages());
        restaurant.setName(req.getName());
        restaurant.setOpeningHours(req.getOpeningHours());
        restaurant.setRegistrationDate(LocalDateTime.now());
        restaurant.setOwner(user);
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant updateRestaurant(Long restaurantId, CreatRestaurantRequest updateRestaurant) throws Exception {
        Restaurant restaurant=findById(restaurantId);
if(restaurant.getDescription()!=null){
    restaurant.setDescription(updateRestaurant.getDescription());
}
if(restaurant.getName()!= null)
{
    restaurant.setName(updateRestaurant.getName());
}
        if (restaurant.getCuisineType() != null) {
            restaurant.setCuisineType(restaurant.getCuisineType());
        }
        if (restaurant.getAddress() != null) {
            restaurant.setAddress(restaurant.getAddress());
        }
        if (restaurant.getContactInformation() != null) {
            restaurant.setContactInformation(restaurant.getContactInformation());
        }
        if (restaurant.getOpeningHours() != null) {
            restaurant.setOpeningHours(restaurant.getOpeningHours());
        }
        if (restaurant.getImages() != null) {
            restaurant.setImages(restaurant.getImages());
        }


        return restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Long restaurantId) throws Exception {
        Restaurant restaurant = findById(restaurantId);

        if (restaurant==null) {
            throw new BadRequestException("Can not Find Restaurant With Id " + restaurantId);
        }

        restaurantRepository.delete(restaurant);


    }

    @Override
    public List<Restaurant> getAllRestaurant() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<Restaurant> searchRestaurant(String keyword) {
        return restaurantRepository.findBySearchQuery(keyword);
    }

    @Override
    public Restaurant updateStatus(Long id) throws Exception {

        Restaurant restaurant = findById(id);

        restaurant.setOpen(!restaurant.isOpen());

        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant findById(Long id) throws Exception {

        Optional<Restaurant> restaurant = restaurantRepository.findById(id);

        if (restaurant.isEmpty()) {
            throw new Exception("Can not Find Restaurant With Id " + id);
        }

        return restaurant.get();
    }

    @Override
    public RestaurantDto addToFavorites(Long restaurantId, User user) throws Exception {


        Restaurant restaurant = findById(restaurantId);

        RestaurantDto restaurantDTO = new RestaurantDto();
        restaurantDTO.setDescription(restaurant.getDescription());
        restaurantDTO.setImages(restaurant.getImages());
        restaurantDTO.setTitle(restaurant.getName());
        restaurantDTO.setId(restaurantId);
        restaurantDTO.setOpen(restaurant.isOpen());


        boolean isFavorite = false;
        List<RestaurantDto> favorites=user.getFavorites();
        for (RestaurantDto favorite : favorites) {
            if (favorite.getId().equals(restaurantId)) {
                isFavorite = true;
                break;
            }
        }

        if (isFavorite) {
            favorites.removeIf(favorite -> favorite.getId().equals(restaurantId));
        } else {
            favorites.add(restaurantDTO);
        }

        userRepository.save(user);

        return restaurantDTO;
    }



    @Override
    public Restaurant getRestaurantByUserId(Long id) throws Exception {
        Restaurant restaurants = restaurantRepository.findByOwnerId(id);

        if (restaurants == null) {
            throw new Exception("Restaurants Can not found By Owner Id " + id);
        }

        return restaurants;
    }
}
