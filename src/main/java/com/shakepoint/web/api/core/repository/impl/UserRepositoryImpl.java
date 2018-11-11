package com.shakepoint.web.api.core.repository.impl;

import com.shakepoint.web.api.core.repository.UserRepository;
import com.shakepoint.web.api.core.service.security.SecurityRole;
import com.shakepoint.web.api.core.util.ShakeUtils;
import com.shakepoint.web.api.data.entity.PartnerProductOrder;
import com.shakepoint.web.api.data.entity.PartnerTrainer;
import com.shakepoint.web.api.data.entity.TrainerInformation;
import com.shakepoint.web.api.data.entity.User;
import com.shakepoint.web.api.data.entity.UserPassword;
import com.shakepoint.web.api.data.entity.UserProfile;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;


/**
 * Users Repository extending the BaseRepository class
 *
 * @author Alberto Rubalcaba
 */
@Stateless
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    private final Logger log = Logger.getLogger(getClass());

    public UserRepositoryImpl() {
    }


    @Override
    @Transactional
    public void saveProfile(UserProfile profile) {
        try {
            em.persist(profile);
        } catch (Exception ex) {
            log.error("Could not add profile", ex);
        }
    }


    private static final String USER_EXISTS = "select count(*) from user where email = ?";

    @Override
    public boolean userExists(String email) {
        try {
            final BigInteger count = (BigInteger) em.createNativeQuery(USER_EXISTS).setParameter(1, email).getSingleResult();
            return count.longValue() > 0;
        } catch (Exception ex) {
            log.error("Could not get user existence", ex);
            return false;
        }
    }

    private static final String GET_LAST_SIGNIN = "select last_signin from user where id = ?";

    @Override
    public String getLastSignin(String id) {

        try {
            return (String) em.createNativeQuery(GET_LAST_SIGNIN).setParameter(1, id).getSingleResult();
        } catch (Exception ex) {
            log.error("Could not get last sign in", ex);
            return null;
        }
    }

    private static final String UPDATE_LAST_SIGNIN = "update user set last_signin = ? where id = ?";

    @Override
    @Transactional
    public void updateLastSignin(String email) {
        //get id
        String id = getUserId(email);
        if (id == null) {
            //super admin user
            return;
        }
        //updates
        try {
            em.createNativeQuery(UPDATE_LAST_SIGNIN).setParameter(1, ShakeUtils.SIMPLE_DATE_FORMAT.format(new Date())).setParameter(2, id).executeUpdate();
        } catch (Exception ex) {
            log.error("Could not update user", ex);
        }
    }

    @Override
    public int getRegisteredTechnicians() {
        try {
            BigInteger count = (BigInteger) em.createNativeQuery(GET_TECHNICIANS_COUNT).getSingleResult();
            return count.intValue();
        } catch (Exception ex) {
            log.error("Could not get technicians count", ex);
            return 0;
        }
    }

    @Override
    public List<User> getUsers() {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.role = 'member'")
                    .getResultList();
        } catch (Exception ex) {
            log.error("Could not get shakepoint users", ex);
            return null;
        }
    }

    private static final String GET_USER_INFO = "select email, password, role from user where email = ?";

    @Override
    public User getUserByEmail(String email) {
        try {
            return (User) em.createQuery("SELECT u FROM User u WHERE u.email = :email")
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception ex) {
            log.error("Could not get user by email", ex);
            return null;
        }
    }

    private static final String GET_TECHNICIANS_COUNT = "select count(*) from user where role = 'technician'";

    @Override
    public List<User> getTechnicians() {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.role = :role")
                    .setParameter("role", SecurityRole.PARTNER.getValue()).getResultList();
        } catch (Exception ex) {
            log.error("Could not get Technicians", ex);
            return null;
        }
    }


    private static final String GET_USER_ID = "select id from user where email = ?";

    @Override
    public String getUserId(String email) {
        try {
            return (String) em.createNativeQuery(GET_USER_ID).setParameter(1, email).getSingleResult();
        } catch (Exception ex) {
            //not found
            return null;
        }
    }

    private static final String GET_TECHNICIAN = "select id, name, email, creation_date from user where id = ? and role = 'technician'";

    @Override
    public User getTechnician(String id) {
        try {
            return (User) em.createQuery("SELECT u FROM User u WHERE u.id = :id AND u.role = :role")
                    .setParameter("role", SecurityRole.PARTNER.getValue())
                    .setParameter("id", id).getSingleResult();
        } catch (Exception ex) {
            log.error("Could not get technician", ex);
            return null;
        }
    }


    @Override
    public UserProfile getUserProfile(String userId) {

        try {
            return (UserProfile) em.createQuery("SELECT p FROM Profile p WHERE p.user.id = :id")
                    .setParameter("id", userId)
                    .getSingleResult();

        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            log.error("Could not get user profile", ex);
            return null;
        }
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void addShakepointUser(User user) {
        try {
            em.persist(user);
        } catch (Exception ex) {
            log.error("Could not persist Entity", ex);
        }
    }

    @Override
    @Transactional
    public void updateProfile(UserProfile existingProfile) {
        try {
            em.merge(existingProfile);
        } catch (Exception ex) {
            log.error("Could not update profile", ex);
        }
    }

    @Override
    @Transactional
    public void saveUserOrder(PartnerProductOrder order) {
        try {
            em.persist(order);
        } catch (Exception ex) {
            log.error("Could not persist requested order", ex);
        }
    }

    @Override
    public User findUserByToken(String token) {
        try {
            return (User) em.createQuery("SELECT u FROM User u WHERE u.accessToken = :token")
                    .setParameter("token", token)
                    .getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    @Transactional
    public void saveUserToken(String id, String token) {
        em.createQuery("UPDATE User u SET u.accessToken = :token WHERE u.id = :id")
                .setParameter("token", token)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public User get(String id) {
        try {
            return (User) em.createQuery("SELECT u FROM User u WHERE u.id = :id")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public UserPassword getUserPassword(String userId) {
        try {
            return (UserPassword) em.createQuery("SELECT u FROM UserPassword u WHERE u.userId = :userId")
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    @Transactional
    public void updateUserPassword(UserPassword userPassword) {
        try {
            em.createNativeQuery("UPDATE user_password SET password_token = ?, token_expiration = ?, reset_count = ? WHERE id = ?")
                    .setParameter(1, userPassword.getToken())
                    .setParameter(2, userPassword.getExpirationDate())
                    .setParameter(3, userPassword.getResetCount())
                    .setParameter(4, userPassword.getId()).executeUpdate();
        } catch (Exception ex) {
            log.error("Could not add user password", ex);
        }
    }

    @Override
    public UserPassword getUserPasswordByToken(String token) {
        try {
            return (UserPassword) em.createQuery("SELECT u FROM UserPassword u WHERE u.token = :token")
                    .setParameter("token", token)
                    .getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<User> getTrainers() {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.role = 'trainer'")
                    .getResultList();
        } catch (Exception ex) {
            log.error("Could not get trainers", ex);
            return null;
        }
    }

    @Override
    @Transactional
    public void addPartnerTrainer(PartnerTrainer partnerTrainer) {
        try {
            em.persist(partnerTrainer);
        } catch (Exception ex) {
            log.error("Could not persis partner trainer relationship", ex);
        }
    }

    @Override
    public List<User> getTrainersForPartner(String id) {
        return em.createQuery("SELECT pt.trainer from PartnerTrainer pt WHERE pt.partner.id = :id")
                .setParameter("id", id).getResultList();
    }

    @Override
    public User getUserByFacebookId(String facebookId) {
        try {
            return (User) em.createQuery("SELECT u.user FROM Profile u WHERE u.facebookId = :id")
                    .setParameter("id", facebookId)
                    .getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void addUserPassword(UserPassword userPassword) {
        try {
            em.persist(userPassword);
        } catch (Exception ex) {
            log.error("Could not persis user password", ex);
        }
    }

    @Override
    public void updateUserPassword(String encryptedPassword, String userId) {
        try {
            em.createNativeQuery("UPDATE user SET password = ? WHERE id = ?")
                    .setParameter(1, encryptedPassword)
                    .setParameter(2, userId)
                    .executeUpdate();
        } catch (Exception ex) {
            log.error("Could not update user password", ex);
        }
    }

    @Transactional
    @Override
    public void createTrainerInformation(TrainerInformation trainerInformation) {
        try {
            em.merge(trainerInformation);
        } catch (Exception ex) {
            log.error("Could not persist trainer information", ex);
        }
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        try{
            em.merge(user);
        } catch(Exception ex) {
            log.error("Could not merge user", ex);
        }
    }


}
