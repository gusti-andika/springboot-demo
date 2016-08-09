package sample.rs.service.impl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.net.URI;

import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import org.apache.cxf.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sample.rs.model.RepositoryItem;
import sample.rs.model.RepositoryItemRepository;
import sample.rs.model.ResultItemContent;
import sample.rs.model.ResultItemType;
import sample.rs.service.RepositoryService;

/**
 * Repository public API implementation with swagger support
 * 
 * @author Andika
 *
 */
@Api("/repository")
@Service
public class RepositoryServiceImpl implements RepositoryService {

	@Autowired
	private RepositoryItemRepository itemRepository;

	@Transactional
	@Override
	@ApiOperation(value = "Register new repository item")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successful registration"),
			@ApiResponse(code = 400, message = "Either itemName, itemContent or itemType not specified"),
			@ApiResponse(code = 409, message = "Repository item with supplied name exists") })
	public Response register(RepositoryItem item) {
		//validate item
		validate(item);
		
		// when repository item already exists with supplied item name
		if (this.getItem(item.getItemName(), false) != null) {
			return Response.status(Response.Status.CONFLICT)
					.location(URI.create("/repository/" + item.getItemName()))
					.build();
		}

		this.itemRepository.save(item);
		return Response
				.created(URI.create("/repository/" + item.getItemName()))
				.build();
	}

	@Override
	@ApiOperation(value = "Delete repository item by it's name")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Successful deletion"),
			@ApiResponse(code = 404, message = "No repository exists with supplied itemName") })
	public Response deregister(String itemName) {
		this.itemRepository.delete(getItem(itemName));
		return Response.noContent().build();
	}

	@Override
	@ApiOperation(value = "Retrieve repository item name", response = ResultItemContent.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful search", response = ResultItemContent.class),
			@ApiResponse(code = 404, message = "No repository exists with supplied itemName") })
	public ResultItemContent retrieve(String itemName) {
		return new ResultItemContent(getItem(itemName));
	}

	@Override
	@ApiOperation(value = "Retrieve repository item type", response = ResultItemType.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful search", response = ResultItemType.class),
			@ApiResponse(code = 404, message = "No repository exists with supplied itemName") })
	public ResultItemType getType(String itemName) {
		return new ResultItemType(getItem(itemName));
	}

	private RepositoryItem getItem(String name) {
		return getItem(name, true);
	}

	private RepositoryItem getItem(String name, boolean throwException) {
		RepositoryItem item = this.itemRepository.findByItemName(name);
		if (item == null && throwException) {
			throw new NotFoundException();
		}
		return item;
	}
	
	private void validate(RepositoryItem item) {
		if (StringUtils.isEmpty(item.getItemName())) {
			throw new BadRequestException("itemName is empty.");
		}
		else if (StringUtils.isEmpty(item.getItemContent())) {
			throw new BadRequestException("itemContent is empty.");
		}
		else if (item.getItemType() == null) {
			throw new BadRequestException("itemType is empty.");
		}
	}
}
