# from imageai import Detection
# import cv2
#
# modelpath = "/Users/kietr/Study/Learning/221/IOT/btl/yolo.h5"
# yolo = Detection.ObjectDetection()
# yolo.setModelTypeAsYOLOv3()
# yolo.setModelPath(modelpath)
# yolo.loadModel()
#
# cam = cv2.VideoCapture(0)
# cam.set(cv2.CAP_PROP_FRAME_WIDTH, 1300)
# cam.set(cv2.CAP_PROP_FRAME_HEIGHT, 1500)
#
#
# while True:
#     ## read frames
#     ret, img = cam.read()
#     ## predict yolo
#     img, preds = yolo.detectObjectsFromImage(input_image=img,
#                       custom_objects=None, input_type="array",
#                       output_type="array",
#                       minimum_percentage_probability=70,
#                       display_percentage_probability=False,
#                       display_object_name=True)
#     ## display predictions
#     cv2.imshow("", img)
#     print(pred)
#     ## press q or Esc to quit
#     if (cv2.waitKey(1) & 0xFF == ord("q")) or (cv2.waitKey(1)==27):
#         break
# ## close camera
# cam.release()
# cv2.destroyAllWindows()

from imageai import Detection
import cv2

modelpath = "/Users/kietr/Study/Learning/221/IOT/btl/yolo.h5"
yolo = Detection.ObjectDetection()
yolo.setModelTypeAsYOLOv3()
yolo.setModelPath(modelpath)
yolo.loadModel()

cam = cv2.VideoCapture(0)
cam.set(cv2.CAP_PROP_FRAME_WIDTH, 1300)
cam.set(cv2.CAP_PROP_FRAME_HEIGHT, 1500)


def image_detector():
    while True:
        ## read frames
        ret, img = cam.read()
        ## predict yolo
        img, preds = yolo.detectObjectsFromImage(input_image=img,
                                                 custom_objects=None, input_type="array",
                                                 output_type="array",
                                                 minimum_percentage_probability=70,
                                                 display_percentage_probability=False,
                                                 display_object_name=True)
        ## display predictions
        cv2.imshow("", img)
        return preds
        # ## press q or Esc to quit
        # if (cv2.waitKey(1) & 0xFF == ord("q")) or (cv2.waitKey(1) == 27):
        #     break
        # ## close camera
        # cam.release()
        # cv2.destroyAllWindows()
