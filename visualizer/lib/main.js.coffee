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

  class Bar
    constructor: () ->
      @checkings = []

    allDone: ()->
      for ele in @checkings
        return false unless ele.called
      true
    
    setCallback: (@callback) ->
    callCallback: ()->
      results = callable.result for callable in @checkings
      @callback.call(results...)

    register: (func) ->
      # find the func in the args, and append the checking logic
      args = [].slice.call(arguments,1)
      @checkings.push(new Callable(func,args,@))

    start: ()->
      for callable in @checkings
        callable.call()
  

  # Load template, for realtime use only
  loadTemplate = () ->
    $('script.dust-template').each (idx,ele) ->
      $ele = $(ele)
      name = $ele.attr('name')

      dust.loadSource(dust.compile($ele.html(),name))


  bar = new Bar()
  bar.register($.get, API('setup'),((data)->(data)),'json')
  bar.register(loadTemplate)

  bar.setCallback (configData)->
    console.log "All Done :)"

  # Load Template
    dust.render("intro",{name:'songyy'},(err,dta) ->
      console.log dta
    )

  bar.start()

  # Get current bus info



  # Show this config info

  # Recurring task
  recurring_task = () ->
    # Pull from the server about the people information, and update DOM
  # setInterval(recurring_task,500)
  recurring_task()
)(dust,$)
