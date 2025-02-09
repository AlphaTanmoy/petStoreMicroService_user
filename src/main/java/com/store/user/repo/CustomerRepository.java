package com.store.user.repo;

import com.store.user.collection.FetchMostRecentInterface;
import com.store.user.collection.GetCustomers;
import com.store.user.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;


public interface CustomerRepository  extends JpaRepository<Customer,String> {

    Customer findByEmail(String email);

    @Query(
            value = "SELECT COUNT(*) FROM customer c WHERE c.email = :email"
            , nativeQuery = true
    )
    Long countUserByEmail(@Param("email") String email);

    @Query(
            value = "SELECT id AS id, created_date AS createdDate " +
                    "FROM customer " +
                    "ORDER BY created_date DESC " +
                    "FETCH FIRST 1 ROW ONLY",
            nativeQuery = true
    )
    Optional<FetchMostRecentInterface> findTop1ByOrderByCreatedDateDesc();

    @Query(
            value = "Select COUNT(*) " +
                    "FROM customer " +
                    "WHERE data_status = :dataStatus " +
                    "AND (:considerFromDate = false OR created_date >= :fromDate) " +
                    "AND (:considerToDate = false OR created_date <= :toDate) "
                    , nativeQuery = true
    )
    Long findCountWithOutOffsetIdAndDate(
            @Param("fromDate") ZonedDateTime fromDate,
            @Param("considerFromDate") Boolean considerFromDate,
            @Param("toDate") ZonedDateTime toDate,
            @Param("considerToDate") Boolean considerToDate,
            @Param("dataStatus") String dataStatus
    );

    @Query(
            value = "Select " +
                    "id as id, " +
                    "full_name as fullName, " +
                    "email as emailId, " +
                    "tire_code as tireCode, " +
                    "role as userRole, " +
                    "is_prime_member as isPrimeMember, " +
                    "created_date as createdDate " +
                    "FROM customer " +
                    "WHERE data_status = :dataStatus " +
                    "AND full_name ILIKE CONCAT('%', :userName, '%') " +
                    "AND (:considerFromDate = false OR created_date >= :fromDate) " +
                    "AND (:considerToDate = false OR created_date <= :toDate) " +
                    "ORDER BY created_date DESC, id DESC"
            , nativeQuery = true
    )
    List<GetCustomers> findDataWithOutOffsetIdAndDate(
            @Param("fromDate") ZonedDateTime fromDate,
            @Param("considerFromDate") Boolean considerFromDate,
            @Param("toDate") ZonedDateTime toDate,
            @Param("considerToDate") Boolean considerToDate,
            @Param("dataStatus") String dataStatus,
            @Param("userName") String userName
    );

    @Query(
            value = "Select " +
                    "id as id, " +
                    "full_name as fullName, " +
                    "email as emailId, " +
                    "tire_code as tireCode, " +
                    "role as userRole, " +
                    "is_prime_member as isPrimeMember, " +
                    "created_date as createdDate " +
                    "FROM customer " +
                    "WHERE created_date < :offsetDateFinal " +
                    "AND data_status = :dataStatus " +
                    "AND full_name ILIKE CONCAT('%', :userName, '%') " +
                    "AND (:considerFromDate = false OR created_date >= :fromDate) " +
                    "AND (:considerToDate = false OR created_date <= :toDate) " +
                    "ORDER BY created_date DESC, id DESC " +
                    "LIMIT :limit"
            , nativeQuery = true
    )
    List<GetCustomers> findDataWithOutOffsetId(
            @Param("fromDate") ZonedDateTime fromDate,
            @Param("considerFromDate") Boolean considerFromDate,
            @Param("toDate") ZonedDateTime toDate,
            @Param("considerToDate") Boolean considerToDate,
            @Param("dataStatus") String dataStatus,
            @Param("userName") String userName,
            @Param("limit") Integer limit,
            @Param("offsetDateFinal") ZonedDateTime offsetDateFinal
    );

    @Query(
            value = "Select " +
                    "id as id, " +
                    "full_name as fullName, " +
                    "email as emailId, " +
                    "tire_code as tireCode, " +
                    "role as userRole, " +
                    "is_prime_member as isPrimeMember, " +
                    "created_date as createdDate " +
                    "FROM customer " +
                    "WHERE id < :offsetId " +
                    "AND created_date = :offsetDateFinal " +
                    "AND data_status = :dataStatus " +
                    "AND full_name ILIKE CONCAT('%', :userName, '%') " +
                    "AND (:considerFromDate = false OR created_date >= :fromDate) " +
                    "AND (:considerToDate = false OR created_date <= :toDate) " +
                    "ORDER BY created_date DESC, id DESC " +
                    "LIMIT :limit"
            , nativeQuery = true
    )
    List<GetCustomers> findDataWithOffsetId(
            @Param("fromDate") ZonedDateTime fromDate,
            @Param("considerFromDate") Boolean considerFromDate,
            @Param("toDate") ZonedDateTime toDate,
            @Param("considerToDate") Boolean considerToDate,
            @Param("dataStatus") String dataStatus,
            @Param("userName") String userName,
            @Param("limit") Integer limit,
            @Param("offsetDateFinal") ZonedDateTime offsetDateFinal,
            @Param("offsetId") String offsetId
    );

}
