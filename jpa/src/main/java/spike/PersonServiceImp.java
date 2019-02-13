package spike;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class PersonServiceImp implements PersonService {

    @Autowired
    private PersonDao userDao;

    @Transactional
    public void add(Person person) {
        userDao.add(person);
    }

    @Transactional(readOnly = true)
    public List<Person> listPersons() {
        return userDao.listPersons();
    }

}
