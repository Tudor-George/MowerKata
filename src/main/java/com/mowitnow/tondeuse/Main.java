package com.mowitnow.tondeuse;

import com.mowitnow.tondeuse.dto.business.*;
import com.mowitnow.tondeuse.dto.helpers.GridMowerDataInput;
import com.mowitnow.tondeuse.io.FileLoader;
import com.mowitnow.tondeuse.mapper.FileToDataInput;
import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.launch.support.JvmSystemExiter;
import org.springframework.batch.core.launch.support.SystemExiter;


public class Main {
	private static final Logger logger = LoggerFactory.getLogger(FileToDataInput.class);
	private SystemExiter systemExiter = new JvmSystemExiter();

	public void exit(int status) {
		systemExiter.exit(status);
	}
	
	public static void main(String[] args) {
		/*
		 * Afin de lancer l'application en mode Batch, il faut ajouter les configuration de la BDD, nous n'avons pas de BDD à rajouter dans l'exemple donnée
		 *  @Autowired
    		JobLauncher jobLauncher;

    		@Autowired
    		Job job;
    		
    		 @Bean
    		public CommandLineRunner commandLineRunner() {
	        	return args -> {
	            jobLauncher.run(job, new JobParameters());
		        };
		    }
		*/

		try {
			
			/*GridMowerDataInput gridMowerDataInput = FileToDataInput.transformFileInput(FileLoader.readFileToRowList(
					"C:\\Users\\hritc\\eclipse-workspace\\gitrepo\\KataTondeuseHritcu\\src\\com\\mowitnow\\tondeuse\\input.txt"));*/
			Main main = new Main();
			if (args.length < 1) {
				logger.error("File path is mandatory !");
				main.exit(1);
			} else {
				GridMowerDataInput gridMowerDataInput = FileToDataInput.transformFileInput(FileLoader.readFileToRowList(args[0]));
				
				Grid grid = new Grid(gridMowerDataInput.getUpperRightCorner());
				grid.setMowerList(new ArrayList<>());
	
				gridMowerDataInput.getMowerDataInputs().stream().forEach(d -> {
					Mower mower = new Mower(new OrientedPosition(d.getPosition(), d.getOrientation()));
					grid.getMowerList().add(mower);
					mower.executeInstructions(grid.getUpperRightCorner(), d.getInstructions());
				});
				logger.info("Final mower positions: {}", grid.toString());
				main.exit(0);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
