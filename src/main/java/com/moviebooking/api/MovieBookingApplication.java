package com.moviebooking.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.moviebooking.api.model.Seat;
import com.moviebooking.api.model.Show;
import com.moviebooking.api.repository.CinemaRepository;
import com.moviebooking.api.repository.MovieRepository;
import com.moviebooking.api.repository.SeatRepository;
import com.moviebooking.api.repository.ShowRepository;
import com.moviebooking.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
@EnableTransactionManagement
public class MovieBookingApplication  implements CommandLineRunner{

	private final CinemaRepository cinemaRepo;
	private final MovieRepository movieRepo;
	private final SeatRepository seatRepo;
	private final ShowRepository showRepo;
	private final UserRepository userRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(MovieBookingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
//		Movie movie1 =new Movie();
//		movie1.setMovieName("Venom");
//		movieRepo.save(movie1);
//		
//		Movie movie2 =new Movie();
//		movie2.setMovieName("Avengers Infinity war");
//		movieRepo.save(movie2);
//		
//		Cinema cinema1 = new Cinema();
//		cinema1.setCinemaName("PVR 1");
//		cinemaRepo.save(cinema1);
//		
//		Cinema cinema2 = new Cinema();
//		cinema2.setCinemaName("PVR 2");
//		cinemaRepo.save(cinema2);
//		
//		Show show11 = new Show();
//		show11.setCinema(cinema1);
//		show11.setMovie(movie1);
//		show11.setShowName("PVR 1 Morning Show");
//		show11.setStartTime(9);
//		show11.setEndTime(12);
//		show11.setVersion(1L);
//		
//		showRepo.save(show11);
//		this.addSeats(show11, 90.0, 30);
//		
//		Show show12 = new Show();
//		show12.setCinema(cinema1);
//		show12.setMovie(movie2);
//		show12.setShowName("PVR 1 Night Show");
//		show12.setStartTime(19);
//		show12.setEndTime(22);
//		show12.setVersion(1L);
//		
//		showRepo.save(show12);
//		this.addSeats(show12, 190.0, 30);
//		
//		Show show21 = new Show();
//		show21.setCinema(cinema2);
//		show21.setMovie(movie1);
//		show21.setShowName("PVR 2 Morning Show");
//		show21.setStartTime(8);
//		show21.setEndTime(11);
//		show21.setVersion(1L);
//		
//		showRepo.save(show21);
//		this.addSeats(show21, 80.0, 30);
//		
//		Show show22 = new Show();
//		show22.setCinema(cinema2);
//		show22.setMovie(movie2);
//		show22.setShowName("PVR 2 Night Show");
//		show22.setStartTime(19);
//		show22.setEndTime(22);
//		show22.setVersion(1L);
//		
//		showRepo.save(show22);
//		this.addSeats(show22, 150.0, 20);
//		
//		
//		User user1 = new User();
//		user1.setUserName("user 1");
//		user1.setPassword("User 1");
//		userRepo.save(user1);
//		
//		User user2 = new User();
//		user2.setUserName("user 2");
//		user2.setPassword("User 2");
//		userRepo.save(user2);
		
	}

	private void addSeats(Show show,Double price,Integer total)
	{
		
		for(int i=0;i<total;i++)
		{
			Seat seat = new Seat();
			seat.setPrice(price);
			seat.setSeatNumber("A"+i);
			seat.setShow(show);
			seat.setVersion(1L);
			seat.setPaymentStatus(false);
			seatRepo.save(seat);
		}
	}
}
