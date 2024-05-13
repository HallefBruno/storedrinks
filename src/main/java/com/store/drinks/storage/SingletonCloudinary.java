package com.store.drinks.storage;

import com.cloudinary.Cloudinary;

public class SingletonCloudinary {

  private static Cloudinary cloudinary;

  public static void registerCloudinary(Cloudinary cloudinary) {
    SingletonCloudinary.cloudinary = cloudinary;
  }

  public static void deregisterCloudinary() {
    cloudinary = null;
  }

  private static class DefaultCloudinaryHolder {

    public static final Cloudinary INSTANCE = new Cloudinary();
  }

  public static Cloudinary getCloudinary() {
    if (cloudinary == null) {
      return DefaultCloudinaryHolder.INSTANCE;
    }
    return cloudinary;
  }
}
