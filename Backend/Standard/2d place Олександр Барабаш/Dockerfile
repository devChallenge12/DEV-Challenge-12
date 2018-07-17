FROM python:3.6
WORKDIR /shared_database
ADD . /shared_database
ENV C_FORCE_ROOT=true
RUN pip install -r requirements.txt
CMD python run.py
