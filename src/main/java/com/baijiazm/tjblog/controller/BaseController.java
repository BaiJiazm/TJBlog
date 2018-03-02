package com.baijiazm.tjblog.controller;

import com.baijiazm.tjblog.utils.MapCache;

public abstract class BaseController {

    protected MapCache cache = MapCache.single();
}
