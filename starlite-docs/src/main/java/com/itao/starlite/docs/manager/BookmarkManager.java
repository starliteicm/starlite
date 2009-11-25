package com.itao.starlite.docs.manager;

import java.util.List;
import java.util.Set;

import com.google.inject.Inject;
import com.itao.starlite.docs.dao.BookmarkDao;
import com.itao.starlite.docs.dao.BookmarkTypeDao;
import com.itao.starlite.docs.dao.TagDao;
import com.itao.starlite.docs.model.Bookmark;
import com.itao.starlite.docs.model.BookmarkType;
import com.itao.starlite.docs.model.Tag;
import com.wideplay.warp.persist.Transactional;

public class BookmarkManager {
	@Inject
	private BookmarkDao bookmarkDao;
	
	@Inject
	private TagDao tagDao;
	
	@Inject
	private BookmarkTypeDao bookmarkTypeDao;
	
	@Transactional
	public List<Bookmark> findAllBookmarksWithTags(String... tags) {
		return bookmarkDao.findBookmarksWithTags(tags);
	}
	
	@Transactional
	public List<Bookmark> search(String query) {
		return bookmarkDao.findBookmarksByQuery(query);
	}
	
	@Transactional
	public BookmarkType getBookmarkTypeByName(String name) {
		return bookmarkTypeDao.findByName(name);
	}

	@Transactional
	public Bookmark createBookmark(String name, String typeName, String bookmarkedId, String... tags) {
		BookmarkType type = bookmarkTypeDao.findByName(typeName);
		if (type == null)
			return null;
		
		Set<Tag> tagSet = tagDao.findTagsCreateIfRequired(tags);
		
		Bookmark b = new Bookmark(tagSet, name, type, bookmarkedId);
		bookmarkDao.makePersistent(b);
		return b;
	}
	
	@Transactional
	public List<Tag> findAllTags() {
		return tagDao.findAll();
	}
	
	@Transactional
	public List<Object[]> findTagsWithCount() {
		return tagDao.findTagsWithCount();
	}
}
