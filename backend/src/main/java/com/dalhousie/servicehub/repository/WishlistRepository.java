package com.dalhousie.servicehub.repository;
import com.dalhousie.servicehub.model.WishlistModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<WishlistModel,Long> {
    List<WishlistModel> findAllByUserId(Long userId);
}
