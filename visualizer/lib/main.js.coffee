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
    for k,v of params.obj
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
  bar.register($.get, API('setup'),((data)->data),'json')
  bar.register(viewBar.start, compileViews)

  # Save this bus info into CONFIG
  CONFIG = {bus_cap:40}
  bar.start (configData)->
    CONFIG['bus_info'] = configData['buses']
    CONFIG['stop_info'] = configData['bus_stops']
    CONFIG['stop_text'] = Object.keys(CONFIG['stop_info'])

    # Build the bus info, for showing
    dataToShow = {}
    buses = []
    for bus_number, stops of CONFIG['bus_info']
      stops_to_show = []
      for stop_idx in stops
        stops_to_show.push
          name:CONFIG['stop_text'][stop_idx]
          stop_id:stop_idx
          bus_number:bus_number

      buses.push
        number: bus_number
        stops: stops_to_show
        cap: CONFIG['bus_cap']

    dataToShow['buses'] = buses

    # Convert config data into show data
    dust.render("bus_info",dataToShow,(err,dta) ->
      $('#bus-info').html(dta)
    )

  # Generate a fake data of the # of passagers
  data_generator = () ->
    # 1. # of people at a certain stop
    # 2. 



  # Show this bus info
  

  # Recurring task
  recurring_task = () ->
    # Pull from the server about the people information, and update DOM
  # setInterval(recurring_task,500)
  recurring_task()
)(dust,$)
