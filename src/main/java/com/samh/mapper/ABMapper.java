package com.samh.mapper;

import com.samh.common.annotation.DataSource;
import org.springframework.stereotype.Repository;

@Repository
public interface ABMapper {
    @DataSource
    void addAge();
}
