package web.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import web.model.User;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public void addUser(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    public void deleteUser(Long id) {
        sessionFactory.getCurrentSession().createQuery("delete User u where u.id=:id").setParameter("id", id).
                executeUpdate();
        }

    @Override
    public void updateUser(User user) {
        sessionFactory.getCurrentSession().update(user);
    }

    @Override
    public List<User> getAllUsers() {
        return sessionFactory.openSession().createQuery("from User").getResultList();
    }

    @Override
    public User getUserByUsername(String username) {
        return sessionFactory.getCurrentSession().createQuery("select u from User u where u.username=:name", User.class).
                setParameter("name", username).getSingleResult();
    }

    @Override
    public User getUserById(Long id) {
        return sessionFactory.getCurrentSession().createQuery("select u from User u where u.id=:id", User.class).
                setParameter("id", id).getSingleResult();
    }
}
