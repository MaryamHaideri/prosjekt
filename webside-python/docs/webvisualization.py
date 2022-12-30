from fastapi import FastAPI, Request
from fastapi.staticfiles import StaticFiles
from fastapi.templating import Jinja2Templates
from webvisualization_plots import plot_reported_cases_per_million, get_countries
from typing import Optional


# create app variable (FastAPI instance)
app = FastAPI()
templates = Jinja2Templates(directory="templates")


# mount one or more static directories,
# e.g. your auto-generated Sphinx documentation with html files
app.mount(
    # the URL where these files will be available
    "/static",
    StaticFiles(
        # the directory the files are in
        directory="static/",
        html=True,
    ),
    # an internal name for FastAPI
    name="static",
)


@app.get("/")
def plot_reported_cases_per_million_html(request: Request):
    """
    Root route for the web application.
    Handle requests that go to the path "/".
    """
    return templates.TemplateResponse(
        "plot_reported_cases_per_million.html",
        {
            "request": request,
            # further template inputs here
            "countries" : get_countries(countries=None, start=None, end=None),
            "start" : None,
            "end" : None,
        },
    )
    
    
@app.get("/plot_reported_cases_per_million.json")
def plot_reported_cases_per_million_json(countries:Optional[str]=None, start:Optional[str]=None, end:Optional[str]=None):
    """Return json chart from altair"""
    if countries:
        countries = countries.split(",")
    if start:
        start = start.split(",")
    if end:
        end = end.split(",")
        
    plot = plot_reported_cases_per_million(countries, start, end)
    #plot = plot_reported_cases_per_million(["Afghanistan", "Albania", "Algeria"])
    #plot = plot_reported_cases_per_million(countries=None, start=None, end=None)
    return plot.to_dict()


def main():
    """Called when run as a script
    Should launch your web app
    """
    import uvicorn
    uvicorn.run(app, host="127.0.0.1", port=8000)


if __name__ == "__main__":
    main()



    
    
"""
      <b> Select an country to add to the chart!</b>  
      <br />
      <select id = "theList" onchange = "refreshPlot()" >  
      <option> ---Choose country--- </option>  
      <option>options</option>  
      </select> 
      <br />
      <br />
      
      
      //script
      //var options = "";
      //for(country in countries){
      //  options += "<option>"+ country +"</option>";
      //}
      //document.getElementById("theList").innerHTML = options;
"""