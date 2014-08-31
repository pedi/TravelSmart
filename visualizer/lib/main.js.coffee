((dust,$) ->

  API_CONFIG =
    bus_progress: "bus_progress.json"
    setup: "setup.json"

  API = (name) -> "api/#{API_CONFIG[name]}"


  getSingleFuncIdxInArr = (arr) ->
    funcIdx = -1

    arr.forEach (ele,idx) ->
      if typeof ele == 'function'
        if funcIdx == -1
          funcIdx = idx
        else
          throw "Duplicate callback function passed in, don't know who to exec"

    funcIdx


  # The Bar Object. The callback would be only called when all registerd func
  # has complete
  Callable = (func, args, bar) ->
    that = this
    this.called = false
    this.func = func
    this.args = args
    this.bar = bar

    # Add logic of storing the returned data
    idx = getSingleFuncIdxInArr(args)

    if idx == -1
      this.noCallback = true
    else
      oldFunc = args[idx]
      args[idx] = (fargs...) ->
        that.result = oldFunc fargs...
        that.called = true
        that.bar.callCallback() if that.bar.allDone()

    this.call = () ->
      if that.noCallback
        that.result = that.func that.args...
        that.called = true
        that.callCallback() if that.bar.allDone()
      else
        that.func that.args...

    this

  Bar = () ->
    that = this
    this.checkings = []

    this.allDone = ()->
      for ele in that.checkings
        return false unless ele.called
      true
    
    this.callCallback = ()->
      results = (callable.result for callable in that.checkings)
      that.callback results...

    this.register = (func) ->
      # find the func in the args, and append the checking logic
      args = [].slice.call(arguments,1)
      that.checkings.push(new Callable(func,args,that))

    this.start = (callback)->
      console.log('starting with callback: ', callback)
      that.callback = callback
      for callable in that.checkings
        callable.call()

    this
  
  # define a Helper to loop through object like an array                            
  # Expecting param to have element obj                                             
  dust.helpers.loopObj = (chk,ctx,bdi,params) ->
    return if(!params.obj)
    return chk.write(params.obj) if(typeof params.obj != "object")
    console.log("rendering, got: ",params)
    for k,v of params.obj
      console.log('rendering: ',k)
      chk.render(bdi.block,ctx.push({key:k,value:v}))
    chk

  viewBar = new Bar()
  registerView = (name) ->
    viewBar.register($.get, "views/#{name}.dust",(tmpl)->{name:name, tmpl:tmpl})
  registerView "bus_info"

  compileViews = () ->
    for viewInfo in arguments
      dust.loadSource(dust.compile(viewInfo.tmpl, viewInfo.name))

  bar = new Bar()
  bar.register($.get, API('setup'),((data)->(data)),'json')
  bar.register(viewBar.start, compileViews)

  bar.start (configData)->
    console.log "All Done :)"
    console.log("Passed in data: ",configData)

    # Load Template
    dust.render("bus_info",configData,(err,dta) ->
      console.log "Err:",err
      console.log "Data: ",dta
    )

  # Get current bus info


  # Show this config info

  # Recurring task
  recurring_task = () ->
    # Pull from the server about the people information, and update DOM
  # setInterval(recurring_task,500)
  recurring_task()
)(dust,$)
