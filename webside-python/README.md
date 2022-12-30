

**requirment**

To run this you have to install:

 pip install altair_viewer



**How is this works**
I Creates pandas dataframe from .csv file. Data will be filtered based on data column name "new_cases_smoothed_per_million", list of countries to be plotted and time frame chosen.
You can change the parameter countries, start and end date on webvisualization_plots.py-file in the main()-method.
Run the task by writing the following command in your terminal:
python3  webvisualization_plots.py
or 
python  webvisualization_plots.py

Then i build a FastAPI app which uses script webvisualization_plots.py to generate a plot of ”Daily new confirmed COVID-19 cases per million people” by date and display it on the web page. 
You can change the input-parameter countries, start and end date on webvisualization.py in plot_reported_cases_per_million_html()-method.
There's also a navigationbar with a help-page. Furthermore, i added a link to the FastAPI docs, that are created automatically, and a link to help-page on the plot page. The help page display help for functions implemented to generate
the plot shown. The help page is generated automatically from docstrings using Sphinx. The code is on docs-directory. 

Here you also can run the task by writing the following command in your terminal:
python3 webvisualization.py

