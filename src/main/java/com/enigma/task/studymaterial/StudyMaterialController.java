package com.enigma.task.studymaterial;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enigma.task.studymaterial.dao.StudyMaterialDao;
import com.enigma.task.studymaterial.dto.CommonResponse;
import com.enigma.task.studymaterial.dto.StudyMaterialDto;
import com.enigma.task.studymaterial.exception.CustomException;
import com.enigma.task.studymaterial.model.StudyMaterial;


@RestController
@RequestMapping("/studymaterial")
@SuppressWarnings("rawtypes")
public class StudyMaterialController {
	
	@Autowired
	public ModelMapper modelMapper;

	@Autowired
	public StudyMaterialDao studyMaterialDao;
	
	@GetMapping(value="/{materialid}")
	public CommonResponse<StudyMaterialDto> getById(@PathVariable("materialid") String materialId) throws CustomException {
		try {
			StudyMaterial studyMaterial = studyMaterialDao.getById(Integer.parseInt(materialId));
			
			return new CommonResponse<StudyMaterialDto>(modelMapper.map(studyMaterial, StudyMaterialDto.class));
			
		} catch (CustomException e) {
			return new CommonResponse<StudyMaterialDto>("06", "input must be a number");
		} catch (Exception e) {
			return new CommonResponse<StudyMaterialDto>("06", e.getMessage());
		}
	}
	
	@PostMapping(value="")
	public CommonResponse<StudyMaterialDto> insert(@RequestBody StudyMaterialDto studyMaterialDto) throws CustomException {
		try {
			StudyMaterial studyMaterial = modelMapper.map(studyMaterialDto, StudyMaterial.class);
			studyMaterial.setMaterialId(0);
			studyMaterial = studyMaterialDao.save(studyMaterial);
			
			return new CommonResponse<StudyMaterialDto>(modelMapper.map(studyMaterial, StudyMaterialDto.class));
		} catch (CustomException e) {
			return new CommonResponse<StudyMaterialDto>("14", "study period not found");
		} catch (NumberFormatException e) {
			return new CommonResponse<StudyMaterialDto>();
		} catch (Exception e) {
			return new CommonResponse<StudyMaterialDto>();
		}
	}
	
	@PutMapping(value="")
	
	public CommonResponse<StudyMaterialDto> update(@RequestBody StudyMaterialDto studyMaterialDto) throws CustomException {
		try {
			StudyMaterial checkStudyMaterial = studyMaterialDao.getById(studyMaterialDto.getMaterialId());
			if (checkStudyMaterial ==  null) {
				return new CommonResponse<StudyMaterialDto>("14", "trainee not found");
			}
			if (studyMaterialDto.getDescription() != null) {
				checkStudyMaterial.setDescription(studyMaterialDto.getDescription());
			}
			if (studyMaterialDto.getActiveFlag() != null) {
				checkStudyMaterial.setActiveFlag(studyMaterialDto.getActiveFlag());
			}
			
			checkStudyMaterial = studyMaterialDao.save(checkStudyMaterial);
			
			return new CommonResponse<StudyMaterialDto>(modelMapper.map(checkStudyMaterial, StudyMaterialDto.class));
			
		} catch (CustomException e) {
			return new CommonResponse("01", e.getMessage());
		} catch (Exception e) {
			return new CommonResponse("06", e.getMessage());
		}
	}
	
	@DeleteMapping(value="/{materialid}")
	public CommonResponse<StudyMaterialDto> delete(@PathVariable("materialid") String materialId) throws CustomException {
		try {
			StudyMaterial studyMaterial = studyMaterialDao.getById(Integer.parseInt(materialId));
			if (studyMaterial == null) {
				return new CommonResponse("06", "trainee not found");
			}
			studyMaterialDao.delete(studyMaterial);
			
			return new CommonResponse<StudyMaterialDto>(modelMapper.map(studyMaterial, StudyMaterialDto.class));
		} catch (CustomException e) {
			return new CommonResponse<StudyMaterialDto>("01", e.getMessage());
		} catch (Exception e) {
			return new CommonResponse<StudyMaterialDto>("06", e.getMessage());
		}
	}
	
	@GetMapping("")
	public CommonResponse<List<StudyMaterialDto>> getList(@RequestParam(name="list", defaultValue="") String mtrId) throws CustomException {
		try {
			List<StudyMaterial> studyMaterials = studyMaterialDao.getList();
			return new CommonResponse<List<StudyMaterialDto>>(studyMaterials.stream().map(stdMaterial -> modelMapper.map(stdMaterial, StudyMaterialDto.class)).collect(Collectors.toList()));
		} catch (CustomException e) {
			throw e;
		} catch(NumberFormatException e) {
			return new CommonResponse("01", e.getMessage());
		} catch (Exception e) {
			return new CommonResponse("06", e.getMessage());
		}
	}
	
	@GetMapping("/active")
	public CommonResponse<List<StudyMaterialDto>> getListByActiveFlag(@RequestParam(name="list", defaultValue="") String mtrId) throws CustomException {
		try {
			List<StudyMaterial> studyMaterials = studyMaterialDao.getList();
			return new CommonResponse<List<StudyMaterialDto>>(studyMaterials.stream().map(materialId -> modelMapper.map(materialId, StudyMaterialDto.class)).collect(Collectors.toList()));
		} catch (CustomException e) {
			throw e;
		} catch(NumberFormatException e) {
			return new CommonResponse("01", e.getMessage());
		} catch (Exception e) {
			return new CommonResponse("06", e.getMessage());
		}
	}
}
