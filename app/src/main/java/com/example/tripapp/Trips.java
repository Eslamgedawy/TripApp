package com.example.tripapp;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

public class Trips
{
  private String tripStatus;
  private String tripNotes;
  private String tripStart;
  private Date updated;
  private String tripName;
  private Integer _id;
  private String tripEnd;
  private String tripDateTime;
  private Date created;
  private String objectId;
  private String ownerId;
  private double sLat;
  private double sLong;
  private double eLat;
  private double eLong;


  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public double getsLat() {
    return sLat;
  }

  public void setsLat(double sLat) {
    this.sLat = sLat;
  }

  public double getsLong() {
    return sLong;
  }

  public void setsLong(double sLong) {
    this.sLong = sLong;
  }

  public double geteLat() {
    return eLat;
  }

  public void seteLat(double eLat) {
    this.eLat = eLat;
  }

  public double geteLong() {
    return eLong;
  }

  public void seteLong(double eLong) {
    this.eLong = eLong;
  }

  public String getTripStatus()
  {
    return tripStatus;
  }

  public void setTripStatus( String tripStatus )
  {
    this.tripStatus = tripStatus;
  }

  public String getTripNotes()
  {
    return tripNotes;
  }

  public void setTripNotes( String tripNotes )
  {
    this.tripNotes = tripNotes;
  }

  public String getTripStart()
  {
    return tripStart;
  }

  public void setTripStart( String tripStart )
  {
    this.tripStart = tripStart;
  }

  public Date getUpdated()
  {
    return updated;
  }

  public String getTripName()
  {
    return tripName;
  }

  public void setTripName( String tripName )
  {
    this.tripName = tripName;
  }

  public Integer get_id()
  {
    return _id;
  }

  public void set_id( Integer _id )
  {
    this._id = _id;
  }

  public String getTripEnd()
  {
    return tripEnd;
  }

  public void setTripEnd( String tripEnd )
  {
    this.tripEnd = tripEnd;
  }

  public String getTripDateTime()
  {
    return tripDateTime;
  }

  public void setTripDateTime( String tripDateTime )
  {
    this.tripDateTime = tripDateTime;
  }

  public Date getCreated()
  {
    return created;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public void setObjectId(String objectId) {
    this.objectId = objectId;
  }

  public String getOwnerId()
  {
    return ownerId;
  }


  public Trips save()
  {
    return Backendless.Data.of( Trips.class ).save( this );
  }

  public void saveAsync( AsyncCallback<Trips> callback )
  {
    Backendless.Data.of( Trips.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Trips.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Trips.class ).remove( this, callback );
  }

  public static Trips findById( String id )
  {
    return Backendless.Data.of( Trips.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<Trips> callback )
  {
    Backendless.Data.of( Trips.class ).findById( id, callback );
  }

  public static Trips findFirst()
  {
    return Backendless.Data.of( Trips.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<Trips> callback )
  {
    Backendless.Data.of( Trips.class ).findFirst( callback );
  }

  public static Trips findLast()
  {
    return Backendless.Data.of( Trips.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<Trips> callback )
  {
    Backendless.Data.of( Trips.class ).findLast( callback );
  }

  public static List<Trips> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( Trips.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<Trips>> callback )
  {
    Backendless.Data.of( Trips.class ).find( queryBuilder, callback );
  }
}