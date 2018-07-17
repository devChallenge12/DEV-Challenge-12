//
//  CameraHandler.swift
//  DevchallengeFinal
//
//  Created by Someone on 6/23/18.
//  Copyright © 2018 Someone. All rights reserved.
//

import UIKit

class CameraHandler: NSObject {
    static let shared = CameraHandler()
    
    fileprivate var currentViewController: UIViewController!
    
    var imagePickedBlock: ((UIImage) -> Void)?
    
    func camera() {
        if UIImagePickerController.isSourceTypeAvailable(.camera) {
            let controller = UIImagePickerController()
            controller.delegate = self
            controller.sourceType = .camera
            currentViewController.present(controller, animated: true, completion: nil)
        }
    }
    
    func photoLibrary() {
        if UIImagePickerController.isSourceTypeAvailable(.photoLibrary) {
            let controller = UIImagePickerController()
            controller.delegate = self
            controller.sourceType = .photoLibrary
            controller.allowsEditing = true
            currentViewController.present(controller, animated: true, completion: nil)
        }
    }
    
    func showActionSheet(viewController: UIViewController) {
        currentViewController = viewController
        let actionSheet = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
        
        actionSheet.addAction(UIAlertAction(title: "Camera", style: .default, handler: { (alert) in
            self.camera()
        }))
        
        actionSheet.addAction(UIAlertAction(title: "Gallery", style: .default, handler: { (alert) in
            self.photoLibrary()
        }))
        
        actionSheet.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
        
        viewController.present(actionSheet, animated: true, completion: nil)
    }
}

extension CameraHandler: UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        if let image = info[UIImagePickerControllerEditedImage] as? UIImage {
            self.imagePickedBlock?(image)
        } else {
            print("Error")
        }
        currentViewController.dismiss(animated: true, completion: nil)
    }
}
