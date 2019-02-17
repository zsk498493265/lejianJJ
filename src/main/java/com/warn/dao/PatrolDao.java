package com.warn.dao;

import com.warn.entity.Cookie;
import com.warn.entity.Patrol;

import java.util.List;

public interface PatrolDao {
      void addPatrolRecords(Patrol patrol);

      Cookie getCookie();

      Patrol getLatestRecord();

      List<Patrol> getAllRecords();

      Integer getOldIdByRecord(Integer point);
}
