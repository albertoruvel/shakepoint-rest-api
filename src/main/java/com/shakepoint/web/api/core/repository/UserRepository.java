/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.api.core.repository;

import com.shakepoint.web.api.data.entity.PartnerProductOrder;
import com.shakepoint.web.api.data.entity.PartnerTrainer;
import com.shakepoint.web.api.data.entity.TrainerInformation;
import com.shakepoint.web.api.data.entity.User;
import com.shakepoint.web.api.data.entity.UserPassword;
import com.shakepoint.web.api.data.entity.UserProfile;

import java.util.List;

public interface UserRepository {
    public User getUserByEmail(String email);
    public List<User> getTechnicians();
    public String getUserId(String email);
    public User getTechnician(String id);
    public List<User> getUsers();
    public int getRegisteredTechnicians(); 
    public void updateLastSignin(String email); 
    public String getLastSignin(String id); 
    public boolean userExists(String email);
    public UserProfile getUserProfile(String userId);
    public void saveProfile(UserProfile profile);

    //JTA
    public void addShakepointUser(User user);

    public void updateProfile(UserProfile existingProfile);

    public void saveUserOrder(PartnerProductOrder order);

    User findUserByToken(String token);

    public void saveUserToken(String id, String token);
    public User get(String id);
    public UserPassword getUserPassword(String userId);

    public void updateUserPassword(UserPassword userPassword);

    public UserPassword getUserPasswordByToken(String token);

    public List<User> getTrainers();

    public void addPartnerTrainer(PartnerTrainer partnerTrainer);

    public List<User> getTrainersForPartner(String id);

    User getUserByFacebookId(String facebookId);

    void addUserPassword(UserPassword userPassword);

    void updateUserPassword(String encryptedPassword, String userId);

    void createTrainerInformation(TrainerInformation trainerInformation);

    public void updateUser(User user);

    public void updateFcmToken(String fcmToken, String id);
}
