package com.shashwat.electronicstorebackend.utilities;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageableResponse<T> {

	private List<T> contentList;
	private int pageNumber;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean lastPage;
	
	public static <U,V> PageableResponse<U> getPageableResponse(Class<U> responseTypeClass ,Page<V> page){
		
		PageableResponse<U> pageableResponse = new PageableResponse<>();
		List<V> pageContentList = page.getContent();
		List<U> dtoList = pageContentList.stream().map(object -> new ModelMapper().map(object, responseTypeClass)).collect(Collectors.toList());
		pageableResponse.setContentList(dtoList);
		pageableResponse.setPageNumber(page.getNumber());
		pageableResponse.setPageSize(page.getSize());
		pageableResponse.setTotalElements(page.getTotalElements());
		pageableResponse.setTotalPages(page.getTotalPages());
		pageableResponse.setLastPage(page.isLast());
		return pageableResponse;
		
	}
	
}
