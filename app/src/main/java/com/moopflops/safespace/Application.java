package com.moopflops.safespace;

import com.moopflops.safespace.engine.model.CarPark;
import com.moopflops.safespace.engine.model.Crime;
import com.moopflops.safespace.ui.Utils;

import java.util.ArrayList;
import java.util.List;

import co.uk.rushorm.android.AndroidInitializeConfig;
import co.uk.rushorm.core.Rush;
import co.uk.rushorm.core.RushCore;

/**
 * Created by tom on 24/10/2015.
 */
public class Application extends android.app.Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Init RushORM because Stu is a noob
    AndroidInitializeConfig config = new AndroidInitializeConfig(getApplicationContext());
    List<Class<? extends Rush>> classes = new ArrayList<>();
    classes.add(CarPark.class);
    classes.add(Crime.class);
    config.setClasses(classes);
    RushCore.initialize(config);
  }
}
