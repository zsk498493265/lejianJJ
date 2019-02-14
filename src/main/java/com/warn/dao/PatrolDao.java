package com.warn.dao;

import com.warn.entity.Cookie;
import com.warn.entity.Patrol;

public interface PatrolDao {
      void addPatrolRecords(Patrol patrol);

      Cookie getCookie();

      Patrol getLatestRecord();
}
