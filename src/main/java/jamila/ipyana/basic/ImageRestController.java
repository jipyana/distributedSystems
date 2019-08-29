package jamila.ipyana.basic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/images")

public class ImageRestController {
	@Autowired
	private MongoJpaRepository images;
	
	@RequestMapping(path="", method=RequestMethod.POST)
	public void createImage(@RequestBody Image img) {
		images.save(img);
		
	}
	
	
	@RequestMapping(path="", method=RequestMethod.GET)
	public Image getImage(@PathVariable Integer imageId) {
		return images.findByImageId(imageId);
	}
}
