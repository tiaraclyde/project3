package core;

/**
 * The Imaging class extends Appointment to add additional functionality
 * for holding a radiology room for technicians.
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class Imaging extends Appointment {
   /**
    * The Radiology room for the imaging appointment.
    */
   private Radiology room;

   /**
    * Constructor for Imaging class
    *
    *@param date  the date of the appointment
    * @param timeslot the timeslot of the appointment
    * @param patient  the patient of the appointment
    * @param provider the provider of the appointment
    * @param room the Radiology room
    */
   public Imaging(Date date, Timeslot timeslot, Person patient,
                  Person provider, Radiology room) {
      super(date, timeslot, patient, provider);
      this.room = room;
   }

   /**
    * Getter method for the Radiology room
    *
    * @return room the Radiology room
    */
   public Radiology getRoom() {
      return room;
   }

   /**
    * Setter method for the Radiology room
    *
    * @param room the Radiology room
     */
   public void setRoom(Radiology room) {
      this.room = room;
   }

   /**
    * Gets string representation of the imaging appointment.
    *
    * @return string version of imaging
    */
   @Override
   public String toString() {
      final String boxFormat = "[%s]";
      return super.toString() + String.format(boxFormat, room);
   }
}
