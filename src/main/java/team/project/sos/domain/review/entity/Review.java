package team.project.sos.domain.review.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import team.project.sos.domain.store.entity.Store;

@Getter
@Entity
@Table(name = "review")
public class Review {

    private Long id;


    private User user;


    private Store store;

}
