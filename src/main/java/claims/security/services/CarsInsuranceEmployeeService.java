package claims.security.services;

import claims.security.entities.CarsInsuranceEmployee;
import claims.security.repositories.CarsInsuranceEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * todo: add log4j2
 */
@Service
public class CarsInsuranceEmployeeService extends  BaseService<CarsInsuranceEmployee> {

    @Autowired
    CarsInsuranceEmployeeRepository carsInsuranceEmployeeRepository;

    public Optional<CarsInsuranceEmployee> findByUsersCode(String coreUserId){
        return this.carsInsuranceEmployeeRepository.findByUsersCode(coreUserId);
    }

}
