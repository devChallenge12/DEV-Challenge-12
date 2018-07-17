//
//  FaceRecognitionManager.swift
//  PrettyFace
//
//  Copyright © 2018 DevChallenge. All rights reserved.
//

import Foundation
import UIKit
import Vision

class FaceRecognitionManager {
    
    static let instance = FaceRecognitionManager()
    private var faceResults: [VNFaceObservation]?
    private var boundingBoxes: [CGRect]? {
        return faceResults?.map {(faceObservation) -> CGRect in
            return faceObservation.boundingBox
        }
    }
    
    func recognize(_ image: UIImage?, orientation: UIImageOrientation, completion: (() -> Void)?) {
        guard let cgImage = image?.cgImage
        else {
            return
        }
        
        let detectRequest = VNDetectFaceLandmarksRequest { (request, error) in
            guard let error = error
                else {
                    guard let results = request.results as? [VNFaceObservation]
                        else {
                            return
                    }
                    self.faceResults = results
                    completion?()
                    return
            }
            
            print(error)
        }
        
        let imageRequestHandler = VNImageRequestHandler(cgImage: cgImage, orientation: self.cgOrientation(orientation), options: [:])
        do {
            try imageRequestHandler.perform([detectRequest])
        } catch (let error) {
            print(error)
        }
    }
    
    private func cgOrientation(_ orientation: UIImageOrientation) -> CGImagePropertyOrientation {
        var cgOrientation = CGImagePropertyOrientation.up
        
        switch orientation {
        case .down:
            cgOrientation = CGImagePropertyOrientation.down
        case .downMirrored:
            cgOrientation = CGImagePropertyOrientation.downMirrored
        case .left:
            cgOrientation = CGImagePropertyOrientation.left
        case .leftMirrored:
            cgOrientation = CGImagePropertyOrientation.leftMirrored
        case .right:
            cgOrientation = CGImagePropertyOrientation.right
        case .rightMirrored:
            cgOrientation = CGImagePropertyOrientation.rightMirrored
        case .up:
            cgOrientation = CGImagePropertyOrientation.up
        case .upMirrored:
            cgOrientation = CGImagePropertyOrientation.upMirrored
        }
        
        return cgOrientation;
    }
    
    func detectedRects(imageOrigin: CGPoint, imageSize: CGSize) -> [CGRect]? {
        let width = imageSize.width
        let height = imageSize.height
        
        return self.boundingBoxes?.map({ (rect) -> CGRect in
            return CGRect(x: rect.origin.x * width + imageOrigin.x,
                          y: (1 - rect.origin.y) * height + imageOrigin.y,
                          width: rect.size.width * width,
                          height: rect.size.height * height)
        })
    }
}
