package com.dalhousie.servicehub.repository;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.model.WishlistModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface WishlistRepository extends JpaRepository<WishlistModel,Long> {
    List<WishlistModel> findAllByUser(UserModel user);
}